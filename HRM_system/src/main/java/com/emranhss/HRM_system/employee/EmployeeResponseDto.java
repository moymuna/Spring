package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.address.AddressResponseDto;
import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.enums.EmploymentType;
import com.emranhss.HRM_system.enums.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeResponseDto {

    private Long id;

    private String contractNo;

    private Date joiningDate;

    private Date dateOfExit;

    private EmployeeStatus status;

    private Date dateOfBirth;

    private Gender gender;

    private String bloodGroup;

    private String employeeCode;

    private EmploymentType employmentType;
    private String image;

    private String bankName;
    private String bankBranch;
    private String bankAccountName;
    private String bankAccountNumber;

    
    private Long departmentId;
    private String departmentName;

    
    private Long designationId;
    private String designationTitle;

    
    private Long officeId;
    private String officeName;

    
    private AddressResponseDto presentAddress;
    private AddressResponseDto permanentAddress;

    
    private Long userId;
    private String fullName;
    private String email;
    private String role;

    
    private Long managerId;
    private String managerName;

}
