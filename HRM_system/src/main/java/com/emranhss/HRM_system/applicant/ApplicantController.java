package com.emranhss.HRM_system.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


    @RestController
    @RequestMapping("/api/applicants")
    @RequiredArgsConstructor
    public class ApplicantController {

        private final ApplicantService applicantService;

        @PostMapping
        @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
        public ApplicantResponseDto create(@RequestBody ApplicantRequestDto dto) {
            return applicantService.create(dto);
        }

        @GetMapping("/{id}")
        @PreAuthorize("@employeeSecurity.isApplicantOwnerOrStaff(#id)")
        public ApplicantResponseDto getById(@PathVariable Long id) {
            return applicantService.getById(id);
        }

        @GetMapping
        @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
        public List<ApplicantResponseDto> getAll() {
            return applicantService.getAll();
        }

        @PutMapping("/{id}")
        @PreAuthorize("@employeeSecurity.isApplicantOwnerOrStaff(#id)")
        public ApplicantResponseDto update(@PathVariable Long id,
                                           @RequestBody ApplicantRequestDto dto) {
            return applicantService.update(id, dto);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
        public void delete(@PathVariable Long id) {
            applicantService.delete(id);
        }

        @GetMapping("/email/{email}")
        @PreAuthorize("@employeeSecurity.isApplicantOwnerByEmailOrStaff(#email)")
        public ApplicantResponseDto getByEmail(@PathVariable String email) {
            return applicantService.getByEmail(email);
        }

        @GetMapping("/search")
        @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
        public List<ApplicantResponseDto> search(@RequestParam String keyword) {
            return applicantService.searchApplicants(keyword);
        }

        @GetMapping("/count")
        @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
        public Long count() {
            return applicantService.getApplicantCount();
        }

        @GetMapping("/page")
        @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
        public Page<ApplicantResponseDto> page(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            Pageable pageable = PageRequest.of(page, size);
            return applicantService.getApplicants(pageable);
        }
    }
