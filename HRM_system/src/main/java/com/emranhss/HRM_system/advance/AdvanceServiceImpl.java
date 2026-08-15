package com.emranhss.HRM_system.advance;

import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.AdvanceStatus;
import com.emranhss.HRM_system.exception.ResourceNotFoundException;
import com.emranhss.HRM_system.exception.ValidationException;
import com.emranhss.HRM_system.notification.NotificationService;
import com.emranhss.HRM_system.salary.Salary;
import com.emranhss.HRM_system.salary.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvanceServiceImpl implements AdvanceService {

    /** An advance may not exceed this many months of gross pay. */
    private static final BigDecimal MAX_GROSS_MULTIPLE = BigDecimal.valueOf(3);

    private static final int MAX_INSTALLMENTS = 12;

    private final AdvanceRepository advanceRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    @Override
    public AdvanceResponseDto saveAdvance(AdvanceRequestDto dto) {

        Employee employee = findEmployee(dto.getEmployeeId());

        validateRequest(dto);

        Advance advance = advanceRepository.save(AdvanceMapper.toEntity(dto, employee));

        auditLogService.record("Advance", advance.getId(), AuditAction.CREATE,
                "Advance of " + advance.getAmount() + " requested by employee " + employee.getId());

        return AdvanceMapper.toResponse(advance);
    }

    @Override
    public AdvanceResponseDto getAdvanceById(Long id) {

        return AdvanceMapper.toResponse(findAdvance(id));
    }

    @Override
    public List<AdvanceResponseDto> getAllAdvances() {

        return advanceRepository.findAll()
                .stream()
                .map(AdvanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvanceResponseDto> getAdvancesByEmployee(Long employeeId) {

        findEmployee(employeeId);

        return advanceRepository.findByEmployeeIdOrderByRequestDateDesc(employeeId)
                .stream()
                .map(AdvanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvanceResponseDto> getAdvancesByStatus(String status) {

        return advanceRepository.findByStatusOrderByRequestDateDesc(parseStatus(status))
                .stream()
                .map(AdvanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdvanceResponseDto updateAdvance(Long id, AdvanceRequestDto dto) {

        Advance advance = findAdvance(id);

        if (advance.getStatus() != AdvanceStatus.PENDING) {
            throw new ValidationException("Only pending advance requests can be edited.");
        }

        Employee employee = findEmployee(dto.getEmployeeId());

        validateRequest(dto);

        AdvanceMapper.updateEntity(advance, dto, employee);

        Advance updated = advanceRepository.save(advance);

        auditLogService.record("Advance", updated.getId(), AuditAction.UPDATE,
                "Advance request updated for employee " + employee.getId());

        return AdvanceMapper.toResponse(updated);
    }

    @Override
    public void deleteAdvance(Long id) {

        Advance advance = findAdvance(id);

        advanceRepository.delete(advance);

        auditLogService.record("Advance", id, AuditAction.DELETE, "Advance request deleted");
    }

    @Override
    public AdvanceResponseDto approveAdvance(Long advanceId) {

        Advance advance = findAdvance(advanceId);

        if (advance.getStatus() != AdvanceStatus.PENDING) {
            throw new ValidationException("Only pending advance requests can be approved.");
        }

        advance.setStatus(AdvanceStatus.APPROVED);
        advance.setDecidedAt(LocalDateTime.now());
        advance.setRejectionReason(null);

        advance = advanceRepository.save(advance);

        auditLogService.record("Advance", advance.getId(), AuditAction.APPROVE,
                "Advance of " + advance.getAmount() + " approved over "
                        + advance.getInstallments() + " installment(s)");

        notificationService.notify(advance.getEmployee().getUser(),
                "Your advance request of " + advance.getAmount() + " was approved. Monthly deduction: "
                        + advance.getMonthlyDeduction() + ".",
                "Advance", advance.getId());

        return AdvanceMapper.toResponse(advance);
    }

    @Override
    public AdvanceResponseDto rejectAdvance(Long advanceId, String rejectionReason) {

        Advance advance = findAdvance(advanceId);

        if (advance.getStatus() != AdvanceStatus.PENDING) {
            throw new ValidationException("Only pending advance requests can be rejected.");
        }

        advance.setStatus(AdvanceStatus.REJECTED);
        advance.setDecidedAt(LocalDateTime.now());
        advance.setRejectionReason(rejectionReason);

        advance = advanceRepository.save(advance);

        auditLogService.record("Advance", advance.getId(), AuditAction.REJECT, rejectionReason);

        notificationService.notify(advance.getEmployee().getUser(),
                "Your advance request of " + advance.getAmount() + " was rejected: " + rejectionReason,
                "Advance", advance.getId());

        return AdvanceMapper.toResponse(advance);
    }

    @Override
    public AdvanceResponseDto markAsPaid(Long advanceId) {

        Advance advance = findAdvance(advanceId);

        if (advance.getStatus() != AdvanceStatus.APPROVED) {
            throw new ValidationException("Only approved advance requests can be marked as paid.");
        }

        advance.setStatus(AdvanceStatus.PAID);

        advance = advanceRepository.save(advance);

        auditLogService.record("Advance", advance.getId(), AuditAction.UPDATE,
                "Advance of " + advance.getAmount() + " disbursed");

        notificationService.notify(advance.getEmployee().getUser(),
                "Your approved advance of " + advance.getAmount() + " has been disbursed.",
                "Advance", advance.getId());

        return AdvanceMapper.toResponse(advance);
    }

    @Override
    public AdvanceResponseDto recordRecovery(Long advanceId, BigDecimal amount) {

        Advance advance = findAdvance(advanceId);

        if (advance.getStatus() != AdvanceStatus.PAID) {
            throw new ValidationException("Recovery can only be recorded against a disbursed advance.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Recovery amount must be greater than zero.");
        }

        if (amount.compareTo(advance.getOutstandingAmount()) > 0) {
            throw new ValidationException("Recovery of " + amount + " exceeds the outstanding "
                    + advance.getOutstandingAmount() + ".");
        }

        BigDecimal recovered = advance.getRecoveredAmount() == null
                ? BigDecimal.ZERO
                : advance.getRecoveredAmount();

        advance.setRecoveredAmount(recovered.add(amount));

        if (advance.getOutstandingAmount().compareTo(BigDecimal.ZERO) == 0) {
            advance.setStatus(AdvanceStatus.SETTLED);
        }

        advance = advanceRepository.save(advance);

        auditLogService.record("Advance", advance.getId(), AuditAction.UPDATE,
                "Recovered " + amount + ", outstanding " + advance.getOutstandingAmount());

        return AdvanceMapper.toResponse(advance);
    }

    @Override
    public List<AdvanceResponseDto> searchAdvances(String keyword) {

        return advanceRepository.searchAdvances(keyword)
                .stream()
                .map(AdvanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long getAdvanceCount() {
        return advanceRepository.count();
    }

    @Override
    public long getPendingCount() {
        return advanceRepository.countByStatus(AdvanceStatus.PENDING);
    }

    @Override
    public Page<AdvanceResponseDto> getAdvances(Pageable pageable) {

        return advanceRepository.findAll(pageable)
                .map(AdvanceMapper::toResponse);
    }

    /**
     * An advance is capped at three months of the employee's gross pay so a request
     * cannot exceed what payroll can realistically recover.
     */
    private void validateRequest(AdvanceRequestDto dto) {

        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Advance amount must be greater than zero.");
        }

        if (dto.getInstallments() != null
                && (dto.getInstallments() < 1 || dto.getInstallments() > MAX_INSTALLMENTS)) {
            throw new ValidationException("Installments must be between 1 and " + MAX_INSTALLMENTS + ".");
        }

        Salary salary = salaryRepository.findByEmployeeIdAndActiveTrue(dto.getEmployeeId()).orElse(null);

        if (salary == null) {
            // No structure on file yet — HR still decides manually, so let the request through.
            return;
        }

        BigDecimal ceiling = salary.getGrossMonthly().multiply(MAX_GROSS_MULTIPLE);

        if (dto.getAmount().compareTo(ceiling) > 0) {
            throw new ValidationException("Advance cannot exceed " + ceiling
                    + " (3 months of gross salary).");
        }
    }

    private Advance findAdvance(Long id) {

        return advanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advance request not found with id : " + id));
    }

    private Employee findEmployee(Long employeeId) {

        if (employeeId == null) {
            throw new ValidationException("Employee is required for an advance request.");
        }

        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id : " + employeeId));
    }

    private AdvanceStatus parseStatus(String status) {

        try {
            return AdvanceStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ValidationException("Unknown advance status : " + status);
        }
    }
}
