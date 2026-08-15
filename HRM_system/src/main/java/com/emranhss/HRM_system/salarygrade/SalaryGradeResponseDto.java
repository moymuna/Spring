package com.emranhss.HRM_system.salarygrade;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalaryGradeResponseDto {

    private Long id;

    private Integer gradeNumber;

    private String title;

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

    private BigDecimal netMonthly;

    private Boolean active;
}
