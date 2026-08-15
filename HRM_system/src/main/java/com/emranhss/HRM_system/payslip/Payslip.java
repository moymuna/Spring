package com.emranhss.HRM_system.payslip;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.PayrollStatus;
import com.emranhss.HRM_system.payroll.Payroll;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payslip")
@AllArgsConstructor
@NoArgsConstructor
public class Payslip {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PayrollStatus status;

    private LocalDateTime generatedAt;

    private LocalDateTime paidAt;

    @OneToOne
    @JoinColumn(name="payroll_id")
    private Payroll payroll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
