package com.emranhss.HRM_system.project;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectRequestDto {
    private String projectName;

    private String description;

    private Date startDate;

    private Date endDate;

    private List<Long> employeeId;
    private Long officeId;
}
