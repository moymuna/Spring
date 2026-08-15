package com.emranhss.HRM_system.jobpost;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.department.DepartmentRepository;
import com.emranhss.HRM_system.enums.JobStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;
    private final DepartmentRepository departmentRepository;







    @Override
    public JobPostResponseDto create(JobPostRequestDto dto) {

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        JobPost jobPost = JobPostMapper.toEntity(dto, department);

        return JobPostMapper.toDTO(jobPostRepository.save(jobPost));
    }

    @Override
    public JobPostResponseDto getById(Long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPost not found"));

        return JobPostMapper.toDTO(jobPost);
    }

    @Override
    public List<JobPostResponseDto> getAll() {
        return jobPostRepository.findAll()
                .stream()
                .map(JobPostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public JobPostResponseDto update(Long id, JobPostRequestDto dto) {

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPost not found"));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        jobPost.setTitle(dto.getTitle());
        jobPost.setDescription(dto.getDescription());
        jobPost.setRequirements(dto.getRequirements());
        jobPost.setLocation(dto.getLocation());
        jobPost.setMinSalary(dto.getMinSalary());
        jobPost.setMaxSalary(dto.getMaxSalary());
        jobPost.setPostedDate(dto.getPostedDate());
        jobPost.setDeadline(dto.getDeadline());
        jobPost.setStatus(dto.getStatus());
        jobPost.setDepartment(department);

        return JobPostMapper.toDTO(jobPostRepository.save(jobPost));
    }

    @Override
    public void delete(Long id) {
        jobPostRepository.deleteById(id);
    }

    @Override
    public List<JobPostResponseDto> getByStatus(JobStatus status) {
        return jobPostRepository.findByStatus(status)
                .stream()
                .map(JobPostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponseDto> getByDepartment(Long departmentId) {
        return jobPostRepository.findByDepartment_Id(departmentId)
                .stream()
                .map(JobPostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponseDto> searchJobPosts(String keyword) {
        return jobPostRepository.searchJobPosts(keyword)
                .stream()
                .map(JobPostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getJobPostCount() {
        return jobPostRepository.count();
    }

    @Override
    public Page<JobPostResponseDto> getJobPosts(Pageable pageable) {
        return jobPostRepository.findAll(pageable)
                .map(JobPostMapper::toDTO);
    }
}
