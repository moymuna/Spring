package com.emranhss.HRM_system.interview;

import com.emranhss.HRM_system.enums.InterviewStatus;
import lombok.Data;

import java.util.Date;
@Data
public class InterviewResponseDto {
    private Long id;

    private Long applicationId;
    private String applicantName;

    private Long interviewerId;
    private String interviewerName;

    private Date interviewDate;

    private String feedback;
    private InterviewStatus result;
}
