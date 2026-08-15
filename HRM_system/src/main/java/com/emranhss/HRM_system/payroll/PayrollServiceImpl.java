package com.emranhss.HRM_system.payroll;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.advance.Advance;
import com.emranhss.HRM_system.advance.AdvanceRepository;
import com.emranhss.HRM_system.attendance.AttendanceMonthlySummaryDto;
import com.emranhss.HRM_system.attendance.AttendanceService;
import com.emranhss.HRM_system.enums.AdvanceStatus;
import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.PayrollStatus;
import com.emranhss.HRM_system.leave.leave.LeaveRepository;
import com.emranhss.HRM_system.notification.NotificationService;
import com.emranhss.HRM_system.salary.Salary;
import com.emranhss.HRM_system.salary.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    private final AdvanceRepository advanceRepository;
    private final LeaveRepository leaveRepository;
    private final AttendanceService attendanceService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    @Override
    public PayrollResponseDto createPayroll(PayrollRequestDto payrollRequestDto) {
        
        Employee employee = employeeRepository.findById(payrollRequestDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + payrollRequestDto.getEmployeeId()));

        
        Payroll payroll = PayrollMapper.toEntity(payrollRequestDto, employee);
        Payroll savedPayroll = payrollRepository.save(payroll);

        return PayrollMapper.toResponse(savedPayroll);
    }

    @Override
    public PayrollResponseDto getPayrollById(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found with id: " + id));

        return PayrollMapper.toResponse(payroll);
    }

    @Override
    public List<PayrollResponseDto> getPayrollsByEmployee(Long employeeId) {
        return payrollRepository.findByEmployeeIdOrderByYearDescMonthDesc(employeeId)
                .stream()
                .map(PayrollMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Builds the monthly sheet from every active salary structure. Where payroll has
     * already been generated the run's own figures are used; where it hasn't, the
     * structure is shown as-is and flagged NOT_GENERATED so HR can see the gap.
     */
    @Override
    public List<SalarySheetRowDto> getSalarySheet(int year, int month) {

        List<SalarySheetRowDto> rows = new java.util.ArrayList<>();

        for (Employee employee : employeeRepository.findAll()) {

            Salary salary = salaryRepository.findByEmployeeIdAndActiveTrue(employee.getId()).orElse(null);
            if (salary == null) {
                continue;
            }

            SalarySheetRowDto row = new SalarySheetRowDto();

            row.setEmployeeId(employee.getId());
            row.setEmployeeCode(employee.getEmployeeCode());
            if (employee.getUser() != null) {
                row.setEmployeeName(employee.getUser().getFullName());
            }
            if (employee.getDepartment() != null) {
                row.setDepartment(employee.getDepartment().getDepartmentName());
            }
            if (employee.getDesignation() != null) {
                row.setDesignation(employee.getDesignation().getTitle());
            }
            if (salary.getSalaryGrade() != null) {
                row.setGradeNumber(salary.getSalaryGrade().getGradeNumber());
            }

            row.setBasicSalary(salary.getBasicSalary());
            row.setHra(salary.getHra());
            row.setConveyanceAllowance(salary.getConveyanceAllowance());
            row.setMedicalAllowance(salary.getMedicalAllowance());
            row.setSpecialAllowance(salary.getSpecialAllowance());
            row.setProvidentFund(salary.getProvidentFund());
            row.setProfessionalTax(salary.getProfessionalTax());
            row.setIncomeTax(salary.getIncomeTax());

            Payroll payroll = payrollRepository
                    .findByEmployeeIdAndMonthAndYear(employee.getId(), month, year)
                    .orElse(null);

            if (payroll != null) {
                row.setGrossSalary(payroll.getGrossSalary());
                row.setTotalDeductions(payroll.getTotalDeductions());
                row.setNetSalary(payroll.getNetSalary());
                row.setPaidDays(payroll.getPaidDays());
                row.setLopDays(payroll.getLopDays());
                row.setUnpaidLeaveDays(payroll.getUnpaidLeaveDays());
                row.setLeaveDeduction(payroll.getLeaveDeduction());
                row.setAdvanceDeduction(payroll.getAdvanceDeduction());
                row.setStatus(payroll.getStatus() == null ? "PROCESSED" : payroll.getStatus().name());
                row.setPayrollId(payroll.getId());
            } else {
                row.setGrossSalary(salary.getGrossMonthly());
                row.setTotalDeductions(salary.getTotalDeductions());
                row.setNetSalary(salary.getGrossMonthly().subtract(salary.getTotalDeductions()));
                row.setLopDays(0);
                row.setUnpaidLeaveDays(0);
                row.setLeaveDeduction(BigDecimal.ZERO);
                row.setAdvanceDeduction(BigDecimal.ZERO);
                row.setStatus("NOT_GENERATED");
            }

            rows.add(row);
        }

        rows.sort(java.util.Comparator.comparing(
                SalarySheetRowDto::getEmployeeCode,
                java.util.Comparator.nullsLast(String::compareTo)));

        return rows;
    }

    @Override
    public List<PayrollResponseDto> getAllPayrolls() {
        return payrollRepository.findAll()
                .stream()
                .map(PayrollMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PayrollResponseDto updatePayroll(Long id, PayrollRequestDto payrollRequestDto) {
        
        Payroll existingPayroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found with id: " + id));

        
        Employee employee = employeeRepository.findById(payrollRequestDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + payrollRequestDto.getEmployeeId()));

        
        PayrollMapper.updateEntity(existingPayroll, payrollRequestDto, employee);
        Payroll updatedPayroll = payrollRepository.save(existingPayroll);

        return PayrollMapper.toResponse(updatedPayroll);
    }

    @Override
    public void deletePayroll(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found with id: " + id));

        payrollRepository.delete(payroll);
    }

    @Override
    public List<PayrollResponseDto> searchPayrolls(String keyword) {
        return payrollRepository.searchPayrolls(keyword)
                .stream()
                .map(PayrollMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long getPayrollCount() {
        return payrollRepository.count();
    }

    @Override
    public Page<PayrollResponseDto> getPayrolls(Pageable pageable) {
        return payrollRepository.findAll(pageable)
                .map(PayrollMapper::toResponse);
    }

    @Override
    public PayrollResponseDto generatePayroll(Long employeeId, int month, int year) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        Salary salary = salaryRepository.findByEmployeeIdAndActiveTrue(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active salary structure found for employee " + employeeId +
                                ". HR must set one up before payroll can be generated."));

        AttendanceMonthlySummaryDto summary = attendanceService.getMonthlySummary(employeeId, year, month);

        int totalDaysInMonth = YearMonth.of(year, month).lengthOfMonth();

        long paidDays = summary.getPresentDays() + summary.getHalfDays() + summary.getOnLeaveDays()
                + summary.getHolidayDays() + summary.getWeekOffDays() + summary.getWorkFromHomeDays();
        long lopDays = summary.getAbsentDays();

        Payroll payroll = payrollRepository
                .findByEmployeeIdAndMonthAndYear(employeeId, month, year)
                .orElseGet(Payroll::new);

        // Advances are only recovered the first time a month is generated. Re-running
        // generation reuses the amount already taken so a balance is never charged twice.
        boolean finalSettlement = isFinalSettlementMonth(employee, month, year);
        BigDecimal advanceDeduction = payroll.getId() == null
                ? recoverAdvanceInstallments(employeeId, month, year, finalSettlement)
                : nz(payroll.getAdvanceDeduction());

        BigDecimal grossSalary = salary.getGrossMonthly();
        BigDecimal perDayRate = grossSalary.divide(BigDecimal.valueOf(totalDaysInMonth), 2, RoundingMode.HALF_UP);
        BigDecimal lopDeduction = perDayRate.multiply(BigDecimal.valueOf(lopDays));

        // Leave on a paid leave type costs nothing; unpaid leave is deducted per day.
        int unpaidLeaveDays = countUnpaidLeaveDays(employeeId, year, month);
        BigDecimal leaveDeduction = perDayRate.multiply(BigDecimal.valueOf(unpaidLeaveDays));

        BigDecimal totalDeductions = salary.getTotalDeductions()
                .add(lopDeduction)
                .add(leaveDeduction)
                .add(advanceDeduction);
        BigDecimal netSalary = grossSalary.subtract(totalDeductions);

        payroll.setEmployee(employee);
        payroll.setMonth(month);
        payroll.setYear(year);
        payroll.setGrossSalary(grossSalary);
        payroll.setTotalDeductions(totalDeductions);
        payroll.setAdvanceDeduction(advanceDeduction);
        payroll.setNetSalary(netSalary);
        payroll.setPaidDays((int) paidDays);
        payroll.setLopDays((int) lopDays);
        payroll.setUnpaidLeaveDays(unpaidLeaveDays);
        payroll.setLeaveDeduction(leaveDeduction);
        payroll.setLopDeduction(lopDeduction);
        payroll.setProvidentFund(salary.getProvidentFund());
        payroll.setProfessionalTax(salary.getProfessionalTax());
        payroll.setIncomeTax(salary.getIncomeTax());
        payroll.setStatus(PayrollStatus.PROCESSED);
        payroll.setGeneratedAt(LocalDateTime.now());

        Payroll saved = payrollRepository.save(payroll);

        auditLogService.record("Payroll", saved.getId(), AuditAction.CREATE,
                "Generated payroll for " + month + "/" + year + ", net: " + netSalary
                        + (advanceDeduction.signum() > 0 ? ", advance recovered: " + advanceDeduction : ""));
        notificationService.notify(employee.getUser(),
                "Your payroll for " + month + "/" + year + " has been generated.",
                "Payroll", saved.getId());

        return PayrollMapper.toResponse(saved);
    }

    /**
     * Marks a generated payroll as paid — the real-world "salary transferred from
     * the company bank account" step. The employee is told which of their accounts
     * the money went to, with the number masked to its last four digits.
     */
    @Override
    public PayrollResponseDto payPayroll(Long id) {

        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found with id: " + id));

        if (payroll.getStatus() == PayrollStatus.PAID) {
            throw new com.emranhss.HRM_system.exception.ValidationException(
                    "This payroll has already been paid.");
        }

        payroll.setStatus(PayrollStatus.PAID);
        payroll.setPaidAt(LocalDateTime.now());

        Payroll saved = payrollRepository.save(payroll);

        Employee employee = saved.getEmployee();
        String account = employee.getBankAccountNumber();
        boolean viaBank = account != null && !account.isBlank();

        auditLogService.record("Payroll", saved.getId(), AuditAction.UPDATE,
                "Salary for " + saved.getMonth() + "/" + saved.getYear() + " paid, net "
                        + saved.getNetSalary()
                        + (viaBank
                        ? " via bank transfer to " + employee.getBankName() + " " + maskAccount(account)
                        : " (no bank account on file — paid manually)"));

        notificationService.notify(employee.getUser(),
                "Your salary for " + saved.getMonth() + "/" + saved.getYear() + " has been paid"
                        + (viaBank
                        ? " to your " + employee.getBankName() + " account " + maskAccount(account)
                        : "")
                        + ".",
                "Payroll", saved.getId());

        return PayrollMapper.toResponse(saved);
    }

    /** Shows only the last four digits, the way a bank statement or payslip would. */
    private static String maskAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) {
            return accountNumber;
        }
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    /**
     * Days of approved unpaid leave that fall inside the payroll month. A leave can
     * straddle month boundaries, so only the overlapping portion is counted.
     */
    private int countUnpaidLeaveDays(Long employeeId, int year, int month) {

        LocalDate monthStart = YearMonth.of(year, month).atDay(1);
        LocalDate monthEnd = YearMonth.of(year, month).atEndOfMonth();

        int days = 0;

        for (var leave : leaveRepository.findUnpaidApprovedLeaves(employeeId, monthStart, monthEnd)) {

            LocalDate from = leave.getStartDate().isBefore(monthStart) ? monthStart : leave.getStartDate();
            LocalDate to = leave.getEndDate().isAfter(monthEnd) ? monthEnd : leave.getEndDate();

            days += (int) ChronoUnit.DAYS.between(from, to) + 1;
        }

        return days;
    }

    /**
     * Takes one installment off every disbursed advance the employee still owes on,
     * marking an advance SETTLED once nothing is outstanding. Returns the total held
     * back this month. On the employee's final settlement the whole balance is taken
     * instead, since no later payroll exists to collect the rest.
     */
    private BigDecimal recoverAdvanceInstallments(Long employeeId, int month, int year,
                                                  boolean finalSettlement) {

        List<Advance> outstanding = advanceRepository
                .findByEmployeeIdAndStatus(employeeId, AdvanceStatus.PAID);

        BigDecimal recoveredThisMonth = BigDecimal.ZERO;

        for (Advance advance : outstanding) {

            BigDecimal remaining = advance.getOutstandingAmount();
            if (remaining.signum() <= 0) {
                continue;
            }

            // The final installment only takes whatever is left.
            BigDecimal installment = finalSettlement
                    ? remaining
                    : advance.getMonthlyDeduction().min(remaining);
            if (installment.signum() <= 0) {
                continue;
            }

            advance.setRecoveredAmount(nz(advance.getRecoveredAmount()).add(installment));

            if (advance.getOutstandingAmount().signum() == 0) {
                advance.setStatus(AdvanceStatus.SETTLED);
                notificationService.notify(advance.getEmployee().getUser(),
                        "Your advance of " + advance.getAmount() + " is now fully repaid.",
                        "Advance", advance.getId());
            }

            advanceRepository.save(advance);

            auditLogService.record("Advance", advance.getId(), AuditAction.UPDATE,
                    "Recovered " + installment + " via payroll " + month + "/" + year
                            + (finalSettlement ? " (final settlement)" : "")
                            + ", outstanding " + advance.getOutstandingAmount());

            recoveredThisMonth = recoveredThisMonth.add(installment);
        }

        return recoveredThisMonth;
    }

    /**
     * The payroll month containing the employee's exit date — or any later month —
     * is their final settlement: whatever advance balance is still owed must be
     * recovered in full, because no future payroll will run for them.
     */
    private boolean isFinalSettlementMonth(Employee employee, int month, int year) {

        if (employee.getDateOfExit() == null) {
            return false;
        }

        YearMonth exitMonth = YearMonth.from(
                Instant.ofEpochMilli(employee.getDateOfExit().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());

        return !exitMonth.isAfter(YearMonth.of(year, month));
    }

    private static BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    @Override
    public java.util.Map<Integer, BigDecimal> getMonthlyCostTrend(int year) {
        java.util.Map<Integer, BigDecimal> result = new java.util.LinkedHashMap<>();
        for (Object[] row : payrollRepository.getMonthlyCostTrend(year)) {
            result.put((Integer) row[0], (BigDecimal) row[1]);
        }
        return result;
    }
}
