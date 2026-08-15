package com.emranhss.HRM_system.application;

import com.emranhss.HRM_system.applicant.Applicant;
import com.emranhss.HRM_system.jobpost.JobPost;

public class ApplicationMapper {public static Application toEntity(
        ApplicationrequestDto dto,
        Applicant applicant,
        JobPost jobPost
) {
    Application application = new Application();

    application.setApplicant(applicant);
    application.setJobPost(jobPost);
    application.setApplyDate(dto.getApplyDate());
    application.setStatus(dto.getStatus());

    return application;
}

    public static ApplicationResponseDto toDTO(Application application) {
        ApplicationResponseDto dto = new ApplicationResponseDto();

        dto.setId(application.getId());

        if (application.getApplicant() != null) {
            dto.setApplicantId(application.getApplicant().getId());
            dto.setApplicantName(application.getApplicant().getName());
        }

        if (application.getJobPost() != null) {
            dto.setJobPostId(application.getJobPost().getId());
            dto.setJobTitle(application.getJobPost().getTitle());
        }

        dto.setApplyDate(application.getApplyDate());
        dto.setStatus(application.getStatus());

        return dto;
    }
}
