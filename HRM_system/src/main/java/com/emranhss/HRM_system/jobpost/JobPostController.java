package com.emranhss.HRM_system.jobpost;


import com.emranhss.HRM_system.enums.JobStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-posts")
@RequiredArgsConstructor
public class JobPostController { private final JobPostService jobPostService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public JobPostResponseDto create(@RequestBody JobPostRequestDto dto) {
        return jobPostService.create(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'APPLICANT')")
    public JobPostResponseDto    getById(@PathVariable Long id) {
        return jobPostService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'APPLICANT')")
    public List<JobPostResponseDto> getAll() {
        return jobPostService.getAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public JobPostResponseDto update(@PathVariable Long id,
                                     @RequestBody JobPostRequestDto dto) {
        return jobPostService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public void delete(@PathVariable Long id) {
        jobPostService.delete(id);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'APPLICANT')")
    public List<JobPostResponseDto> getByStatus(@PathVariable JobStatus status) {
        return jobPostService.getByStatus(status);
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'APPLICANT')")
    public List<JobPostResponseDto> getByDepartment(@PathVariable Long departmentId) {
        return jobPostService.getByDepartment(departmentId);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'APPLICANT')")
    public List<JobPostResponseDto> search(@RequestParam String keyword) {
        return jobPostService.searchJobPosts(keyword);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'APPLICANT')")
    public Long count() {
        return jobPostService.getJobPostCount();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'APPLICANT')")
    public Page<JobPostResponseDto> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jobPostService.getJobPosts(pageable);
    }
}
