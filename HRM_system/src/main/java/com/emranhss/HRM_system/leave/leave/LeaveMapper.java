package com.emranhss.HRM_system.leave.leave;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.LeaveStatus;
import com.emranhss.HRM_system.leave.leavetype.LeaveType;

public class LeaveMapper {
    
    public static Leave toEntity(LeaveRequestDto dto,
                                 Employee employee,
                                 LeaveType leaveType) {

        Leave leave = new Leave();

        
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setTotalDays(dto.getTotalDays());
        leave.setReason(dto.getReason());
        leave.setStatus(LeaveStatus.valueOf(dto.getStatus()));
        leave.setDecidedAt(dto.getDecidedAt());
        leave.setRejectionReason(dto.getRejectionReason());

        
        leave.setEmployee(employee);
        leave.setLeaveType(leaveType);

        return leave;
    }

    
    public static LeaveResponseDto toResponse(Leave leave) {

        LeaveResponseDto dto = new LeaveResponseDto();

        
        dto.setId(leave.getId());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setTotalDays(leave.getTotalDays());
        dto.setReason(leave.getReason());
        dto.setStatus(String.valueOf(leave.getStatus()));
        dto.setDecidedAt(leave.getDecidedAt());
        dto.setRejectionReason(leave.getRejectionReason());

        
        if (leave.getEmployee() != null) {

            dto.setEmployeeId(leave.getEmployee().getId());
            dto.setEmployeeName(leave.getEmployee().getUser().getFullName());
        }

        
        if (leave.getLeaveType() != null) {

            dto.setLeaveTypeId(leave.getLeaveType().getId());
            dto.setLeaveTypeName(leave.getLeaveType().getName());
        }

        return dto;
    }

    
    public static void updateEntity(Leave leave,
                                    LeaveRequestDto dto,
                                    Employee employee,
                                    LeaveType leaveType) {

        
        
        
        
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setTotalDays(dto.getTotalDays());
        leave.setReason(dto.getReason());
        leave.setStatus(LeaveStatus.valueOf(dto.getStatus()));

        
        leave.setEmployee(employee);
        leave.setLeaveType(leaveType);
    }

}
