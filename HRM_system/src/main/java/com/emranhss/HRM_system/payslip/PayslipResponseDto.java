package com.emranhss.HRM_system.payslip;

import com.emranhss.HRM_system.enums.PayrollStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayslipResponseDto {
    private Long id;

    private Integer month;

    private Integer year;

    private BigDecimal grossSalary;

    private BigDecimal totalDeductions;

    private BigDecimal netSalary;

    private Integer paidDays;

    private Integer lopDays;

    /* Deduction breakdown, read from the payroll this payslip was cut from. */
    private Integer unpaidLeaveDays;

    private BigDecimal leaveDeduction;

    private BigDecimal advanceDeduction;

    private BigDecimal lopDeduction;

    private BigDecimal providentFund;

    private BigDecimal professionalTax;

    private BigDecimal incomeTax;

    private PayrollStatus status;

    private LocalDateTime generatedAt;

    private LocalDateTime paidAt;

    private Long employeeId;
    private String employeeName;

    /* Payment details: where the salary was credited (account shown masked). */
    private String bankName;
    private String bankAccountNumber;

    private Long payrollId;
}
