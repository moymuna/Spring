package com.emranhss.HRM_system.application;

import com.emranhss.HRM_system.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ApplicationService { ApplicationResponseDto applyJob(ApplicationrequestDto dto);

    ApplicationResponseDto getById(Long id);

    List<ApplicationResponseDto > getAll();

    List<ApplicationResponseDto > getByApplicant(Long applicantId);

    List<ApplicationResponseDto> getByJobPost(Long jobPostId);

    List<ApplicationResponseDto> getByStatus(ApplicationStatus status);

    ApplicationResponseDto updateStatus(Long id, ApplicationStatus status);

    void delete(Long id);

    List<ApplicationResponseDto> searchApplications(String keyword);

    long getApplicationCount();

    Page<ApplicationResponseDto> getApplications(Pageable pageable);
}
