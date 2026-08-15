package com.emranhss.HRM_system.department;

import lombok.Data;

@Data
public class DepartmentRequestDto {

    private String departmentName;
    private String code;

    private Long departmentHeadId;
    private Long officeId;
}
