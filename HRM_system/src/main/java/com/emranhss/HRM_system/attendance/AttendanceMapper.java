package com.emranhss.HRM_system.attendance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.AttendanceStatus;

public class AttendanceMapper {
    
    public static Attendance toEntity(AttendanceRequestDto dto, Employee employee) {

        Attendance attendance = new Attendance();

        
        attendance.setDate(dto.getDate());
        attendance.setCheckInTime(dto.getCheckInTime());
        attendance.setCheckOutTime(dto.getCheckOutTime());
        attendance.setStatus(AttendanceStatus.valueOf(dto.getStatus()));

        
        attendance.setEmployee(employee);

        return attendance;
    }

    
    public static AttendanceResponseDto toResponse(Attendance attendance) {

        AttendanceResponseDto dto = new AttendanceResponseDto();

        
        dto.setId(attendance.getId());
        dto.setDate(attendance.getDate());
        dto.setCheckInTime(attendance.getCheckInTime());
        dto.setCheckOutTime(attendance.getCheckOutTime());
        dto.setStatus(String.valueOf(attendance.getStatus()));

        
        dto.setWorkedHours(attendance.getWorkedHours());

        
        if (attendance.getEmployee() != null) {

            dto.setEmployeeId(attendance.getEmployee().getId());

            dto.setEmployeeName(attendance.getEmployee().getUser().getFullName());
        }

        return dto;
    }

    
    public static void updateEntity(Attendance attendance,
                                    AttendanceRequestDto dto,
                                    Employee employee) {

        
        attendance.setDate(dto.getDate());
        attendance.setCheckInTime(dto.getCheckInTime());
        attendance.setCheckOutTime(dto.getCheckOutTime());
        attendance.setStatus(AttendanceStatus.valueOf(dto.getStatus()));

        
        attendance.setEmployee(employee);
    }
}
