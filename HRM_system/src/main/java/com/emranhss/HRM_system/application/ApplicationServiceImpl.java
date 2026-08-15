package com.emranhss.HRM_system.application;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.applicant.*;
import com.emranhss.HRM_system.enums.ApplicationStatus;
import com.emranhss.HRM_system.jobpost.JobPost;
import com.emranhss.HRM_system.jobpost.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicantRepository applicantRepository;
    private final JobPostRepository jobPostRepository;









    @Override
    public ApplicationResponseDto applyJob(ApplicationrequestDto dto) {

        Applicant applicant = applicantRepository.findById(dto.getApplicantId())
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not found"));

        JobPost jobPost = jobPostRepository.findById(dto.getJobPostId())
                .orElseThrow(() -> new ResourceNotFoundException("JobPost not found"));

        Application application = ApplicationMapper.toEntity(dto, applicant, jobPost);

        application.setStatus(ApplicationStatus.APPLIED);

        return ApplicationMapper.toDTO(applicationRepository.save(application));
    }

    @Override
    public ApplicationResponseDto getById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        return ApplicationMapper.toDTO(application);
    }

    @Override
    public List<ApplicationResponseDto> getAll() {
        return applicationRepository.findAll()
                .stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponseDto> getByApplicant(Long applicantId) {
        return applicationRepository.findByApplicant_Id(applicantId)
                .stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponseDto> getByJobPost(Long jobPostId) {
        return applicationRepository.findByJobPost_Id(jobPostId)
                .stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponseDto> getByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status)
                .stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationResponseDto updateStatus(Long id, ApplicationStatus status) {

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        application.setStatus(status);

        return ApplicationMapper.toDTO(applicationRepository.save(application));
    }

    @Override
    public void delete(Long id) {
        applicationRepository.deleteById(id);
    }

    @Override
    public List<ApplicationResponseDto> searchApplications(String keyword) {
        return applicationRepository.searchApplications(keyword)
                .stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getApplicationCount() {
        return applicationRepository.count();
    }

    @Override
    public Page<ApplicationResponseDto> getApplications(Pageable pageable) {
        return applicationRepository.findAll(pageable)
                .map(ApplicationMapper::toDTO);
    }
}
