package com.emranhss.HRM_system.jobpost;

import com.emranhss.HRM_system.enums.JobStatus;
import lombok.Data;

import java.util.Date;

@Data
public class JobPostResponseDto {
    private Long id;

    private String title;
    private String description;
    private String requirements;
    private String location;

    private Double minSalary;
    private Double maxSalary;

    private Date postedDate;
    private Date deadline;

    private JobStatus status;

    private Long departmentId;
    private String departmentName;
}
