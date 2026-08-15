package com.emranhss.HRM_system.salary;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.salarygrade.SalaryGrade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "salary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;


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

    @OneToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_grade_id")
    private SalaryGrade salaryGrade;
}
