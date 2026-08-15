package com.emranhss.HRM_system.project;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectResponseDto {
    private Long id;

    private String projectName;

    private String description;

    private Date startDate;

    private Date endDate;

    private List<Long> employeeId;
    private List<String> employeeName;
    private Long officeId;
    private String officeName;
}
