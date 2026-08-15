package com.emranhss.HRM_system.training;

import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.employee.Employee;


public class TrainingMapper {
    
    public static Training toEntity(TrainingRequestDto dto,
                                    Employee employee, Department department) {

        Training training = new Training();


        String trTitle= dto.getTrainingTitle();
        training.setTrainingTitle(trTitle);
        training.setStartDate(dto.getStartDate());
        training.setEndDate(dto.getEndDate());
        training.setEmployee(employee);
        training.setDepartment(department);

        return training;
    }

    
    public static TrainingResponseDto toResponse(Training training) {

        TrainingResponseDto dto = new TrainingResponseDto();

        dto.setId(training.getId());
        dto.setTrainingTitle(training.getTrainingTitle());
        dto.setStartDate(training.getStartDate());
        dto.setEndDate(training.getEndDate());

        if (training.getEmployee() != null) {
            dto.setEmployeeId(training.getEmployee().getId());
            dto.setEmployeeName(training.getEmployee().getUser().getFullName());
        }
        if (training.getDepartment() != null) {

            dto.setDepartmentId(training.getDepartment().getId());

            dto.setDepartmentName(
                    training.getDepartment().getDepartmentName());
        }

        dto.setStatus(training.getStatus());
        dto.setRejectionReason(training.getRejectionReason());

        return dto;
    }

    
    public static void updateEntity(Training training,
                                    TrainingRequestDto dto,
                                    Employee employee,Department department) {

        training.setTrainingTitle(dto.getTrainingTitle());
        training.setStartDate(dto.getStartDate());
        training.setEndDate(dto.getEndDate());
        training.setEmployee(employee);
        training.setDepartment(department);
    }

}
