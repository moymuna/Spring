package com.emranhss.HRM_system.application;

import com.emranhss.HRM_system.enums.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole( 'APPLICANT')")
    public ApplicationResponseDto applyJob(@RequestBody ApplicationrequestDto dto) {
        return applicationService.applyJob(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isApplicationOwnerOrStaff(#id)")
    public ApplicationResponseDto getById(@PathVariable Long id) {
        return applicationService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<ApplicationResponseDto> getAll() {
        return applicationService.getAll();
    }

    @GetMapping("/applicant/{applicantId}")
    @PreAuthorize("@employeeSecurity.isApplicantOwnerForApplicantIdOrStaff(#applicantId)")
    public List<ApplicationResponseDto> getByApplicant(@PathVariable Long applicantId) {
        return applicationService.getByApplicant(applicantId);
    }

    @GetMapping("/job/{jobPostId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'APPLICANT')")
    public List<ApplicationResponseDto> getByJobPost(@PathVariable Long jobPostId) {
        return applicationService.getByJobPost(jobPostId);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<ApplicationResponseDto> getByStatus(@PathVariable ApplicationStatus status) {
        return applicationService.getByStatus(status);
    }

    @PatchMapping ("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ApplicationResponseDto updateStatus(@PathVariable Long id,
                                               @RequestParam ApplicationStatus status) {
        return applicationService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public void delete(@PathVariable Long id) {
        applicationService.delete(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<ApplicationResponseDto> search(@RequestParam String keyword) {
        return applicationService.searchApplications(keyword);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public Long count() {
        return applicationService.getApplicationCount();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public Page<ApplicationResponseDto> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return applicationService.getApplications(pageable);
    }
}
