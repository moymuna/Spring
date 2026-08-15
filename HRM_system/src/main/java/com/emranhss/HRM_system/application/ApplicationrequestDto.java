package com.emranhss.HRM_system.application;

import com.emranhss.HRM_system.enums.ApplicationStatus;
import lombok.Data;

import java.util.Date;
@Data
public class ApplicationrequestDto {
    private Long applicantId;
    private Long jobPostId;

    private Date applyDate;
    private ApplicationStatus status;
}
