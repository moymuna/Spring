package com.emranhss.HRM_system.performancereview;

import com.emranhss.HRM_system.employee.Employee;

public class PerformanceReviewMapper {

    
    public static PerformanceReview toEntity(
            PerformanceReviewRequestDto dto,
            Employee employee,
            Employee reviewer) {

        PerformanceReview review = new PerformanceReview();

        review.setReviewPeriodStart(dto.getReviewPeriodStart());
        review.setReviewPeriodEnd(dto.getReviewPeriodEnd());
        review.setRating(dto.getRating());
        review.setStrengths(dto.getStrengths());
        review.setAreasForImprovement(dto.getAreasForImprovement());
        review.setComments(dto.getComments());
        review.setStatus(dto.getStatus());

        review.setEmployee(employee);
        review.setReviewer(reviewer);

        return review;
    }

    
    public static PerformanceReviewResponseDto toResponse(
            PerformanceReview review) {

        PerformanceReviewResponseDto dto = new PerformanceReviewResponseDto();

        dto.setId(review.getId());
        dto.setReviewPeriodStart(review.getReviewPeriodStart());
        dto.setReviewPeriodEnd(review.getReviewPeriodEnd());
        dto.setRating(review.getRating());
        dto.setStrengths(review.getStrengths());
        dto.setAreasForImprovement(review.getAreasForImprovement());
        dto.setComments(review.getComments());
        dto.setStatus(review.getStatus());

        if (review.getEmployee() != null) {
            dto.setEmployeeId(review.getEmployee().getId());
            dto.setEmployeeName(review.getEmployee().getUser().getFullName());
        }

        if (review.getReviewer() != null) {
            dto.setReviewerId(review.getReviewer().getId());
            dto.setReviewerName(review.getReviewer().getUser().getFullName());
        }

        return dto;
    }

    
    public static void updateEntity(
            PerformanceReview review,
            PerformanceReviewRequestDto dto,
            Employee employee,
            Employee reviewer) {

        review.setReviewPeriodStart(dto.getReviewPeriodStart());
        review.setReviewPeriodEnd(dto.getReviewPeriodEnd());
        review.setRating(dto.getRating());
        review.setStrengths(dto.getStrengths());
        review.setAreasForImprovement(dto.getAreasForImprovement());
        review.setComments(dto.getComments());
        review.setStatus(dto.getStatus());

        review.setEmployee(employee);
        review.setReviewer(reviewer);
    }
}
