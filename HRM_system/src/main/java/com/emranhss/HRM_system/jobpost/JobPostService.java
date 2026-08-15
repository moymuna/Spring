package com.emranhss.HRM_system.jobpost;

import com.emranhss.HRM_system.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobPostService {
    JobPostResponseDto create(JobPostRequestDto  dto);

    JobPostResponseDto getById(Long id);

    List<JobPostResponseDto > getAll();

    JobPostResponseDto update(Long id, JobPostRequestDto dto);

    void delete(Long id);

    List<JobPostResponseDto> getByStatus(JobStatus status);

    List<JobPostResponseDto> getByDepartment(Long departmentId);

    List<JobPostResponseDto> searchJobPosts(String keyword);

    long getJobPostCount();

    Page<JobPostResponseDto> getJobPosts(Pageable pageable);
}
