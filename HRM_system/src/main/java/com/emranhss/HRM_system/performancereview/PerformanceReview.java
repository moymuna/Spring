package com.emranhss.HRM_system.performancereview;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "PerformanceReview")
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate reviewPeriodStart;

    @Column(nullable = false)
    private LocalDate reviewPeriodEnd;

    
    @Column(precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(length = 2000)
    private String strengths;

    @Column(length = 2000)
    private String areasForImprovement;

    @Column(length = 2000)
    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private Employee reviewer;

}
