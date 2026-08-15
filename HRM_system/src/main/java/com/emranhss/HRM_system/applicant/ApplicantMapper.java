package com.emranhss.HRM_system.applicant;

public class ApplicantMapper {

    public static Applicant toEntity(ApplicantRequestDto dto) {

        Applicant applicant = new Applicant();

        applicant.setName(dto.getName());

        applicant.setEmail(dto.getEmail());

        applicant.setPhone(dto.getPhone());

        applicant.setAddress(dto.getAddress());


        applicant.setEducation(dto.getEducation());


        applicant.setExperience(dto.getExperience());


        applicant.setSkills(dto.getSkills());

        applicant.setCvPath(dto.getCvPath());


        return applicant;
    }



    public static ApplicantResponseDto toDTO(Applicant applicant) {

        ApplicantResponseDto dto = new ApplicantResponseDto();


        dto.setId(applicant.getId());

        dto.setName(applicant.getName());

        dto.setEmail(applicant.getEmail());

        dto.setPhone(applicant.getPhone());

        dto.setAddress(applicant.getAddress());


        dto.setEducation(applicant.getEducation());


        dto.setExperience(applicant.getExperience());


        dto.setSkills(applicant.getSkills());

        dto.setCvPath(applicant.getCvPath());


        return dto;
    }


}
