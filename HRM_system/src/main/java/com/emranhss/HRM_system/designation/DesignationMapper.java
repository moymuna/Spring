package com.emranhss.HRM_system.designation;

import com.emranhss.HRM_system.department.Department;

public class DesignationMapper {
    
    public static Designation toEntity(DesignationRequestDto dto,
                                       Department department) {

        Designation designation = new Designation();

        
        designation.setTitle(dto.getTitle());
        designation.setLevel(dto.getLevel());

        
        designation.setDepartment(department);

        return designation;
    }

    
    public static DesignationResponseDto toResponse(Designation designation) {

        DesignationResponseDto dto = new DesignationResponseDto();

        
        dto.setId(designation.getId());
        dto.setTitle(designation.getTitle());
        dto.setLevel(designation.getLevel());

        
        if (designation.getDepartment() != null) {

            dto.setDepartmentId(designation.getDepartment().getId());

            dto.setDepartmentName(designation.getDepartment().getDepartmentName());
        }

        return dto;
    }

    
    public static void updateEntity(Designation designation,
                                    DesignationRequestDto dto,
                                    Department department) {

        
        designation.setTitle(dto.getTitle());
        designation.setLevel(dto.getLevel());

        
        designation.setDepartment(department);
    }
}
