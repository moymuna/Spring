package com.emranhss.HRM_system.payroll;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.payslip.Payslip;
import com.emranhss.HRM_system.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payroll")
@AllArgsConstructor
@NoArgsConstructor
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer month; 

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal grossSalary;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalDeductions;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal netSalary;

    private Integer paidDays;


    private Integer lopDays;

    /** Approved days this month taken on an unpaid leave type. */
    private Integer unpaidLeaveDays;

    /** Portion of totalDeductions caused by those unpaid leave days. */
    @Column(precision = 12, scale = 2)
    private BigDecimal leaveDeduction = BigDecimal.ZERO;

    /**
     * Portion of totalDeductions that repaid salary advances this month. Held
     * separately so a regenerated payroll can reuse it instead of recovering twice.
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal advanceDeduction = BigDecimal.ZERO;

    /** Portion of totalDeductions caused by absent (loss-of-pay) days. */
    @Column(precision = 12, scale = 2)
    private BigDecimal lopDeduction = BigDecimal.ZERO;

    /*
     * Statutory components copied from the salary structure at generation time, so
     * the payslip breakdown stays true even if the structure is edited later.
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal providentFund = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal professionalTax = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal incomeTax = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PayrollStatus status;

    private LocalDateTime generatedAt;

    private LocalDateTime paidAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToOne(mappedBy = "payroll")
    private Payslip payslip;
}
