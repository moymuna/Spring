package com.emranhss.HRM_system.performancereview;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PerformanceReviewService {

    
    PerformanceReviewResponseDto createPerformanceReview(
            PerformanceReviewRequestDto dto);

    
    List<PerformanceReviewResponseDto> getAllPerformanceReviews();

    
    PerformanceReviewResponseDto getPerformanceReviewById(Long id);

    
    PerformanceReviewResponseDto updatePerformanceReview(
            Long id,
            PerformanceReviewRequestDto dto);

    
    void deletePerformanceReview(Long id);


}
