package com.emranhss.HRM_system.applicant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApplicantService {
    ApplicantResponseDto create(ApplicantRequestDto dto);

    ApplicantResponseDto     getById(Long id);

    List<ApplicantResponseDto> getAll();

    ApplicantResponseDto update(Long id, ApplicantRequestDto dto);

    void delete(Long id);

    ApplicantResponseDto  getByEmail(String email);

    List<ApplicantResponseDto> searchApplicants(String keyword);

    long getApplicantCount();

    Page<ApplicantResponseDto> getApplicants(Pageable pageable);
}
