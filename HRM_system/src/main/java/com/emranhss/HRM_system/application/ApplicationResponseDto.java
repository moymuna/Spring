package com.emranhss.HRM_system.application;

import com.emranhss.HRM_system.enums.ApplicationStatus;
import lombok.Data;

import java.util.Date;
@Data
public class ApplicationResponseDto {
    private Long id;

    private Long applicantId;
    private String applicantName;

    private Long jobPostId;
    private String jobTitle;

    private Date applyDate;
    private ApplicationStatus status;
}
