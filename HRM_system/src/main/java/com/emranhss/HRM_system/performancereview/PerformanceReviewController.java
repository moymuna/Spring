package com.emranhss.HRM_system.performancereview;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performancereview")
@RequiredArgsConstructor
public class PerformanceReviewController {
    private final PerformanceReviewService performanceReviewService;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<PerformanceReviewResponseDto> createPerformanceReview(
            @RequestBody PerformanceReviewRequestDto dto) {

        PerformanceReviewResponseDto response =
                performanceReviewService.createPerformanceReview(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<PerformanceReviewResponseDto>> getAllPerformanceReviews() {

        return ResponseEntity.ok(
                performanceReviewService.getAllPerformanceReviews()
        );
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<PerformanceReviewResponseDto> getPerformanceReviewById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                performanceReviewService.getPerformanceReviewById(id)
        );
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<PerformanceReviewResponseDto> updatePerformanceReview(
            @PathVariable Long id,
            @RequestBody PerformanceReviewRequestDto dto) {

        PerformanceReviewResponseDto response =
                performanceReviewService.updatePerformanceReview(id, dto);

        return ResponseEntity.ok(response);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<String> deletePerformanceReview(
            @PathVariable Long id) {

        performanceReviewService.deletePerformanceReview(id);

        return ResponseEntity.ok("Performance Review deleted successfully.");
    }
}
