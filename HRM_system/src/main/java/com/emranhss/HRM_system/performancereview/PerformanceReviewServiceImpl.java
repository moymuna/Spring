package com.emranhss.HRM_system.performancereview;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceReviewServiceImpl implements PerformanceReviewService {
    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeRepository employeeRepository;

    
    @Override
    public PerformanceReviewResponseDto createPerformanceReview(
            PerformanceReviewRequestDto dto) {

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id : "
                                + dto.getEmployeeId()));

        
        Employee reviewer = employeeRepository.findById(dto.getReviewerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reviewer not found with id : "
                                + dto.getReviewerId()));

        
        PerformanceReview review =
                PerformanceReviewMapper.toEntity(dto, employee, reviewer);

        
        PerformanceReview savedReview =
                performanceReviewRepository.save(review);

        return PerformanceReviewMapper.toResponse(savedReview);
    }

    
    @Override
    public List<PerformanceReviewResponseDto> getAllPerformanceReviews() {

        return performanceReviewRepository.findAll()
                .stream()
                .map(PerformanceReviewMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public PerformanceReviewResponseDto getPerformanceReviewById(Long id) {

        PerformanceReview review = performanceReviewRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Performance Review not found with id : " + id));

        return PerformanceReviewMapper.toResponse(review);
    }

    
    @Override
    public PerformanceReviewResponseDto updatePerformanceReview(
            Long id,
            PerformanceReviewRequestDto dto) {

        
        PerformanceReview review = performanceReviewRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Performance Review not found with id : " + id));

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id : "
                                + dto.getEmployeeId()));

        
        Employee reviewer = employeeRepository.findById(dto.getReviewerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reviewer not found with id : "
                                + dto.getReviewerId()));

        
        PerformanceReviewMapper.updateEntity(
                review,
                dto,
                employee,
                reviewer
        );

        
        PerformanceReview updatedReview =
                performanceReviewRepository.save(review);

        return PerformanceReviewMapper.toResponse(updatedReview);
    }

    
    @Override
    public void deletePerformanceReview(Long id) {

        PerformanceReview review = performanceReviewRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Performance Review not found with id : " + id));

        performanceReviewRepository.delete(review);
    }
}
