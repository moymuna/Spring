package com.emranhss.HRM_system.leave.leave;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;
import com.emranhss.HRM_system.exception.ValidationException;

import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.LeaveStatus;
import com.emranhss.HRM_system.leave.leavebalance.LeaveBalance;
import com.emranhss.HRM_system.leave.leavebalance.LeaveBalanceRepository;
import com.emranhss.HRM_system.leave.leavetype.LeaveType;
import com.emranhss.HRM_system.leave.leavetype.LeaveTypeRepository;
import com.emranhss.HRM_system.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService{
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    
    @Override
    public LeaveResponseDto saveLeave(LeaveRequestDto dto) {

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        
        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave type not found."));

        
        Leave leave = LeaveMapper.toEntity(dto, employee, leaveType);

        
        leave = leaveRepository.save(leave);

        
        return LeaveMapper.toResponse(leave);
    }

    
    @Override
    public LeaveResponseDto getLeaveById(Long id) {

        
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave not found."));

        return LeaveMapper.toResponse(leave);
    }

    
    @Override
    public List<LeaveResponseDto> getAllLeaves() {

        return leaveRepository.findAll()
                .stream()
                .map(LeaveMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public LeaveResponseDto updateLeave(Long id, LeaveRequestDto dto) {

        
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave not found."));

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        
        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave type not found."));

        
        LeaveMapper.updateEntity(leave, dto, employee, leaveType);

        
        leave = leaveRepository.save(leave);

        
        return LeaveMapper.toResponse(leave);
    }

    
    @Override
    public void deleteLeave(Long id) {


        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave not found."));

        // An approved leave already consumed balance days; give them back so
        // deleting the record doesn't leak the employee's entitlement.
        if (leave.getStatus() == LeaveStatus.APPROVED) {
            refundBalance(leave);
        }

        leaveRepository.delete(leave);

        auditLogService.record("Leave", id, AuditAction.DELETE,
                leave.getTotalDays() + " day(s) of " + leave.getLeaveType().getName()
                        + " deleted" + (leave.getStatus() == LeaveStatus.APPROVED ? ", balance refunded" : ""));
    }

    
    @Override
    public List<LeaveResponseDto> getLeavesByEmployee(Long employeeId) {

        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        
        return leaveRepository.findByEmployee(employee)
                .stream()
                .map(LeaveMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public List<LeaveResponseDto> getLeavesByStatus(String status) {

        LeaveStatus leaveStatus = LeaveStatus.valueOf(status.toUpperCase());

        return leaveRepository.findByStatus(leaveStatus)
                .stream()
                .map(LeaveMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public List<LeaveResponseDto> getLeavesBetweenDates(LocalDate startDate,
                                                        LocalDate endDate) {

        return leaveRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(LeaveMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public LeaveResponseDto approveLeave(Long leaveId) {

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found."));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new ValidationException("Only pending leave requests can be approved.");
        }

        // Unpaid leave is not balance-tracked — its cost is the payroll deduction,
        // so approval needs no balance record and consumes nothing.
        if (leave.getLeaveType().isPaid()) {

            int year = leave.getStartDate().getYear();

            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeAndLeaveTypeAndYear(leave.getEmployee(), leave.getLeaveType(), year)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No leave balance record found for this employee/leave type/" + year +
                                    ". HR must create one before this leave can be approved."));

            if (leave.getTotalDays() > balance.getRemaining()) {
                throw new ValidationException(
                        "Cannot approve: requested " + leave.getTotalDays() +
                                " day(s) exceeds remaining balance of " + balance.getRemaining() + " day(s).");
            }

            balance.setUsed(balance.getUsed() + leave.getTotalDays());
            leaveBalanceRepository.save(balance);
        }

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setDecidedAt(LocalDateTime.now());
        leave.setRejectionReason(null);

        leave = leaveRepository.save(leave);

        auditLogService.record("Leave", leave.getId(), AuditAction.APPROVE,
                leave.getTotalDays() + " day(s) of " + leave.getLeaveType().getName());
        notificationService.notify(leave.getEmployee().getUser(),
                "Your leave request (" + leave.getStartDate() + " to " + leave.getEndDate() + ") was approved.",
                "Leave", leave.getId());

        return LeaveMapper.toResponse(leave);
    }

    @Override
    public LeaveResponseDto rejectLeave(Long leaveId,
                                        String rejectionReason) {

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found."));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new ValidationException("Only pending leave requests can be rejected.");
        }

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setDecidedAt(LocalDateTime.now());
        leave.setRejectionReason(rejectionReason);

        leave = leaveRepository.save(leave);

        auditLogService.record("Leave", leave.getId(), AuditAction.REJECT, rejectionReason);
        notificationService.notify(leave.getEmployee().getUser(),
                "Your leave request (" + leave.getStartDate() + " to " + leave.getEndDate() + ") was rejected: " + rejectionReason,
                "Leave", leave.getId());

        return LeaveMapper.toResponse(leave);
    }

    /**
     * Cancels a pending or approved leave. Cancelling an approved leave returns
     * the consumed days to the employee's balance — leave days, like money, must
     * never silently vanish.
     */
    @Override
    public LeaveResponseDto cancelLeave(Long leaveId) {

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found."));

        if (leave.getStatus() != LeaveStatus.PENDING && leave.getStatus() != LeaveStatus.APPROVED) {
            throw new ValidationException("Only pending or approved leave requests can be cancelled.");
        }

        if (leave.getStatus() == LeaveStatus.APPROVED) {
            refundBalance(leave);
        }

        leave.setStatus(LeaveStatus.CANCELLED);
        leave.setDecidedAt(LocalDateTime.now());

        leave = leaveRepository.save(leave);

        auditLogService.record("Leave", leave.getId(), AuditAction.UPDATE,
                leave.getTotalDays() + " day(s) of " + leave.getLeaveType().getName() + " cancelled");
        notificationService.notify(leave.getEmployee().getUser(),
                "Your leave (" + leave.getStartDate() + " to " + leave.getEndDate() + ") has been cancelled.",
                "Leave", leave.getId());

        return LeaveMapper.toResponse(leave);
    }

    /** Returns the days an approved paid leave took from the year's balance. */
    private void refundBalance(Leave leave) {

        if (!leave.getLeaveType().isPaid()) {
            return; // unpaid leave never consumed a balance
        }

        int year = leave.getStartDate().getYear();

        leaveBalanceRepository
                .findByEmployeeAndLeaveTypeAndYear(leave.getEmployee(), leave.getLeaveType(), year)
                .ifPresent(balance -> {
                    balance.setUsed(Math.max(0.0, balance.getUsed() - leave.getTotalDays()));
                    leaveBalanceRepository.save(balance);
                });
    }

    @Override
    public List<LeaveResponseDto> searchLeaves(String keyword) {
        return leaveRepository.searchLeaves(keyword)
                .stream()
                .map(LeaveMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long getLeaveCount() {
        return leaveRepository.count();
    }

    @Override
    public Page<LeaveResponseDto> getLeaves(Pageable pageable) {
        return leaveRepository.findAll(pageable)
                .map(LeaveMapper::toResponse);
    }
}
