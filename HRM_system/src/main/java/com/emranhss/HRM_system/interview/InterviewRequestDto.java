package com.emranhss.HRM_system.interview;

import com.emranhss.HRM_system.enums.InterviewStatus;
import lombok.Data;

import java.util.Date;
@Data
public class InterviewRequestDto {
    private Long applicationId;
    private Long interviewerId;

    private Date interviewDate;

    private String feedback;
    private InterviewStatus result;
}
