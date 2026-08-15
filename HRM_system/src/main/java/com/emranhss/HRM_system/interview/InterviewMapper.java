package com.emranhss.HRM_system.interview;

import com.emranhss.HRM_system.application.Application;

import com.emranhss.HRM_system.user.User;

public class InterviewMapper {
    public static Interview toEntity(
            InterviewRequestDto dto,
            Application application,
            User interviewer
    ) {
        Interview interview = new Interview();

        interview.setApplication(application);
        interview.setInterviewer(interviewer);
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setFeedback(dto.getFeedback());
        interview.setResult(dto.getResult());

        return interview;
    }

    public static InterviewResponseDto toDTO(Interview interview) {
        InterviewResponseDto dto = new InterviewResponseDto();

        dto.setId(interview.getId());

        if (interview.getApplication() != null) {
            dto.setApplicationId(interview.getApplication().getId());
            dto.setApplicantName(interview.getApplication().getApplicant().getName());
        }

        if (interview.getInterviewer() != null) {
            dto.setInterviewerId(interview.getInterviewer().getId());
            dto.setInterviewerName(interview.getInterviewer().getFullName());
        }

        dto.setInterviewDate(interview.getInterviewDate());
        dto.setFeedback(interview.getFeedback());
        dto.setResult(interview.getResult());

        return dto;
    }
}
