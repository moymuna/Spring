package com.emranhss.HRM_system.interview;

import java.util.List;

public interface InterviewService {
    InterviewResponseDto scheduleInterview(InterviewRequestDto  dto);

    InterviewResponseDto getById(Long id);

    List<InterviewResponseDto   > getAll();

    List<InterviewResponseDto>  getByApplication(Long applicationId);

    List<InterviewResponseDto> getByInterviewer(Long interviewerId);

    InterviewResponseDto updateInterview(Long id, InterviewRequestDto dto);

    void delete(Long id);
}
