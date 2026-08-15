package com.emranhss.HRM_system.project;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.office.Office;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {

    
    public static Project toEntity(ProjectRequestDto dto,
                                   List<Employee> employees, Office office) {

        Project project = new Project();

        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        project.setEmployee(employees);
        project.setOffice(office);

        return project;
    }

    
    public static ProjectResponseDto toResponse(Project project) {

        ProjectResponseDto dto = new ProjectResponseDto();

        dto.setId(project.getId());
        dto.setProjectName(project.getProjectName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        if (project.getEmployee() != null) {
            dto.setEmployeeName(
                    project.getEmployee()
                            .stream()
                            .map(emp -> emp.getUser() != null ? emp.getUser().getFullName() : "No Name")
                            .collect(Collectors.toList())
            );
            dto.setEmployeeId(
                    project.getEmployee()
                            .stream()
                            .map(Employee::getId)
                            .collect(Collectors.toList())
            );

        }
        if (project.getOffice() != null) {

            dto.setOfficeId(project.getOffice().getId());

            dto.setOfficeName(project.getOffice().getOfficeName());
        }

        return dto;
    }

    
    public static void updateEntity(Project project,
                                    ProjectRequestDto dto,
                                    List<Employee> employee ,  Office office) {

        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setOffice(office);
        project.setEmployee(employee);
    }
}
