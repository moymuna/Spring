package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.enums.EmploymentType;
import lombok.Data;

import java.util.Date;

/**
 * Employment terms supplied when a hired applicant is turned into an employee.
 * Personal details (name, email, phone, address) carry over from the applicant,
 * so only the things HR decides at hire time are asked for here.
 */
@Data
public class HireRequestDto {

    private Date joiningDate;

    private EmploymentType employmentType;

    private Long departmentId;

    private Long designationId;

    private Long officeId;

    private Long managerId;

    /** Optional — a code is generated when this is left blank. */
    private String employeeCode;

    private String contractNo;
}
