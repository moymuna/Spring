package com.emranhss.HRM_system.performancereview;

import com.emranhss.HRM_system.enums.ReviewStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PerformanceReviewRequestDto {
    private LocalDate reviewPeriodStart;

    private LocalDate reviewPeriodEnd;

    private BigDecimal rating;

    private String strengths;

    private String areasForImprovement;

    private String comments;

    private ReviewStatus status;

    private Long employeeId;

    private Long reviewerId;
}
