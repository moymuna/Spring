package com.emranhss.HRM_system.salary;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalaryRequestDto {

    private BigDecimal basicSalary;

    private BigDecimal hra;

    private BigDecimal conveyanceAllowance;

    private BigDecimal medicalAllowance;

    private BigDecimal specialAllowance;

    private BigDecimal providentFund;

    private BigDecimal professionalTax;

    private BigDecimal incomeTax;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean active;

    private Long employeeId;

    /**
     * Pay scale the structure is built from. When set, any component left blank
     * is filled in from the grade, so grades 1-15 produce different salaries.
     */
    private Long salaryGradeId;
}
