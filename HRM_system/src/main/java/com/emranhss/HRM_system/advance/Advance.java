package com.emranhss.HRM_system.advance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.AdvanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Salary advance an employee requests and HR/Admin decides on. Once approved the
 * amount is recovered over a number of monthly installments.
 */
@Entity
@Table(name = "advance_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate requestDate;

    /** Month the employee wants the advance released in. */
    private LocalDate requiredByDate;

    @Column(nullable = false)
    private Integer installments = 1;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdvanceStatus status = AdvanceStatus.PENDING;

    private LocalDateTime decidedAt;

    @Column(length = 500)
    private String rejectionReason;

    /** How much has already been recovered from payroll. */
    @Column(precision = 12, scale = 2)
    private BigDecimal recoveredAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Transient
    public BigDecimal getMonthlyDeduction() {

        if (amount == null || installments == null || installments < 1) {
            return BigDecimal.ZERO;
        }

        return amount.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);
    }

    @Transient
    public BigDecimal getOutstandingAmount() {

        BigDecimal total = amount == null ? BigDecimal.ZERO : amount;
        BigDecimal recovered = recoveredAmount == null ? BigDecimal.ZERO : recoveredAmount;

        return total.subtract(recovered).max(BigDecimal.ZERO);
    }
}
