package com.emranhss.HRM_system.designation;

import lombok.Data;

@Data
public class DesignationRequestDto {
    private String title;
    private String level;

    private Long departmentId;
}
