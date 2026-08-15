package com.emranhss.HRM_system.applicant;

import com.emranhss.HRM_system.enums.EducationLevel;
import com.emranhss.HRM_system.enums.ExperienceLevel;
import lombok.Data;

import java.util.List;

@Data
public class ApplicantRequestDto {

    private String name;

    private String email;

    private String phone;

    private String address;


    private List<EducationLevel> education;


    private List<ExperienceLevel> experience;


    private String skills;

    private String cvPath;


    
    private String password;


}
