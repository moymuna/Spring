package com.emranhss.HRM_system.salarygrade;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Admin-managed pay scale. Grade 1 is the highest, grade 15 the lowest, and every
 * employee's salary structure is built from the grade it points at.
 */
@Entity
@Table(name = "salary_grade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_number", nullable = false, unique = true)
    private Integer gradeNumber;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal basicSalary;

    @Column(precision = 12, scale = 2)
    private BigDecimal hra = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal conveyanceAllowance = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal medicalAllowance = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal specialAllowance = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal providentFund = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal professionalTax = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal incomeTax = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean active = true;

    @Transient
    public BigDecimal getGrossMonthly() {
        return nz(basicSalary).add(nz(hra)).add(nz(conveyanceAllowance))
                .add(nz(medicalAllowance)).add(nz(specialAllowance));
    }

    @Transient
    public BigDecimal getTotalDeductions() {
        return nz(providentFund).add(nz(professionalTax)).add(nz(incomeTax));
    }

    private static BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
