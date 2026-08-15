package com.emranhss.HRM_system.salary;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalaryResponseDto {
    private Long id;

    private BigDecimal basicSalary;

    private BigDecimal hra;

    private BigDecimal conveyanceAllowance;

    private BigDecimal medicalAllowance;

    private BigDecimal specialAllowance;

    private BigDecimal providentFund;

    private BigDecimal professionalTax;

    private BigDecimal incomeTax;

    private BigDecimal grossMonthly;

    private BigDecimal totalDeductions;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean active;

    private BigDecimal netMonthly;

    private Long employeeId;

    private String employeeName;

    private String employeeCode;

    private Long salaryGradeId;

    private Integer gradeNumber;

    private String gradeTitle;
}
