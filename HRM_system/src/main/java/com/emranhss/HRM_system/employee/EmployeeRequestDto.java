package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.address.AddressRequestDto;
import com.emranhss.HRM_system.address.AddressResponseDto;
import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.enums.EmploymentType;
import com.emranhss.HRM_system.enums.Gender;
import com.emranhss.HRM_system.enums.Role;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeRequestDto {


    
    private String contractNo;
    private Date joiningDate;
    private Date dateOfBirth;
    private EmployeeStatus status;
    private Gender gender;
    private String bloodGroup;
    private String employeeCode;
    private EmploymentType employmentType;
    private String image;

    private String bankName;
    private String bankBranch;
    private String bankAccountName;
    private String bankAccountNumber;


    
    private String fullName;
    private String email;
    private String password;
    private Role role;

    
    private Long departmentId;
    private Long designationId;
    private Long officeId;
    private Long managerId;



    private AddressRequestDto  presentAddress;
    private AddressRequestDto permanentAddress;



}
