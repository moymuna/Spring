package com.emranhss.HRM_system.department;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.office.Office;

public class DepartmentMapper {
    
    public static Department toEntity(DepartmentRequestDto dto,
                                      Employee departmentHead,
                                      Office office) {

        Department department = new Department();

        
        department.setDepartmentName(dto.getDepartmentName());
        department.setCode(dto.getCode());

        
        department.setDepartmentHead(departmentHead);
        department.setOffice(office);

        return department;
    }

    
    public static DepartmentResponseDto toResponse(Department department) {

        DepartmentResponseDto dto = new DepartmentResponseDto();

        
        dto.setId(department.getId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setCode(department.getCode());

        
        if (department.getDepartmentHead() != null) {

            dto.setDepartmentHeadId(
                    department.getDepartmentHead().getId());

            dto.setDepartmentHeadName(
                    department.getDepartmentHead().getUser().getFullName());
        }

        
        if (department.getOffice() != null) {

            dto.setOfficeId(
                    department.getOffice().getId());

            dto.setOfficeName(
                    department.getOffice().getOfficeName());
        }

        return dto;
    }

    
    public static void updateEntity(Department department,
                                    DepartmentRequestDto dto,
                                    Employee departmentHead,
                                    Office office) {

        
        department.setDepartmentName(dto.getDepartmentName());
        department.setCode(dto.getCode());

        
        department.setDepartmentHead(departmentHead);
        department.setOffice(office);
    }
}
