package com.emranhss.HRM_system.department;

import lombok.Data;

@Data
public class DepartmentResponseDto {
    private Long id;

    private String departmentName;
    private String code;

    private Long departmentHeadId;
    private String departmentHeadName;

    private Long officeId;
    private String officeName;
}
