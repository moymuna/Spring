package com.emranhss.HRM_system.jobpost;

import com.emranhss.HRM_system.department.Department;

public class JobPostMapper {
    public static JobPost toEntity(JobPostRequestDto dto, Department department) {
        JobPost jobPost = new JobPost();

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

        return jobPost;
    }

    public static JobPostResponseDto toDTO(JobPost jobPost) {
        JobPostResponseDto dto = new JobPostResponseDto();

        dto.setId(jobPost.getId());
        dto.setTitle(jobPost.getTitle());
        dto.setDescription(jobPost.getDescription());
        dto.setRequirements(jobPost.getRequirements());
        dto.setLocation(jobPost.getLocation());
        dto.setMinSalary(jobPost.getMinSalary());
        dto.setMaxSalary(jobPost.getMaxSalary());
        dto.setPostedDate(jobPost.getPostedDate());
        dto.setDeadline(jobPost.getDeadline());
        dto.setStatus(jobPost.getStatus());

        if (jobPost.getDepartment() != null) {
            dto.setDepartmentId(jobPost.getDepartment().getId());
            dto.setDepartmentName(jobPost.getDepartment().getDepartmentName());
        }

        return dto;
    }
}
