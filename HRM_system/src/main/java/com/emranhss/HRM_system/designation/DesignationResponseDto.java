package com.emranhss.HRM_system.designation;

import lombok.Data;

@Data
public class DesignationResponseDto {
    private Long id;

    private String title;
    private String level;

    private Long departmentId;
    private String departmentName;
}
