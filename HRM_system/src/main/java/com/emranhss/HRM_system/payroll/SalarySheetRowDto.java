package com.emranhss.HRM_system.payroll;

import lombok.Data;

import java.math.BigDecimal;

/** One employee's line on the monthly salary sheet. */
@Data
public class SalarySheetRowDto {

    private Long employeeId;

    private String employeeCode;

    private String employeeName;

    private String department;

    private String designation;

    private Integer gradeNumber;

    /* Earnings */
    private BigDecimal basicSalary;

    private BigDecimal hra;

    private BigDecimal conveyanceAllowance;

    private BigDecimal medicalAllowance;

    private BigDecimal specialAllowance;

    private BigDecimal grossSalary;

    /* Deductions */
    private BigDecimal providentFund;

    private BigDecimal professionalTax;

    private BigDecimal incomeTax;

    private Integer lopDays;

    private Integer unpaidLeaveDays;

    private BigDecimal leaveDeduction;

    private BigDecimal advanceDeduction;

    private BigDecimal totalDeductions;

    private BigDecimal netSalary;

    private Integer paidDays;

    /** PROCESSED / PAID when a payroll exists, otherwise NOT_GENERATED. */
    private String status;

    private Long payrollId;
}
