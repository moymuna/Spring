package com.emranhss.HRM_system.performancereview;

import com.emranhss.HRM_system.enums.ReviewStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PerformanceReviewResponseDto {
    private Long id;

    private LocalDate reviewPeriodStart;

    private LocalDate reviewPeriodEnd;

    private BigDecimal rating;

    private String strengths;

    private String areasForImprovement;

    private String comments;

    private ReviewStatus status;

    private Long employeeId;

    private String employeeName;

    private Long reviewerId;

    private String reviewerName;
}
