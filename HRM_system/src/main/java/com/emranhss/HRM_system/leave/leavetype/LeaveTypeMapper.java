package com.emranhss.HRM_system.leave.leavetype;

import com.emranhss.HRM_system.enums.LeaveStatus;
import com.emranhss.HRM_system.enums.LeavesType;

public class LeaveTypeMapper {

    
    public static LeaveType toEntity(LeaveTypeRequestDto dto) {

        LeaveType leaveType = new LeaveType();

        
        leaveType.setName(LeavesType.valueOf(dto.getName().name()));
        leaveType.setMaxDaysPerYear(dto.getMaxDaysPerYear());
        leaveType.setMaxCarryForwardDays(
                dto.getMaxCarryForwardDays() == null ? 0 : dto.getMaxCarryForwardDays());
        leaveType.setPaid(dto.getPaid());
        leaveType.setDescription(dto.getDescription());

        return leaveType;
    }

    
    public static LeaveTypeResponseDto toResponse(LeaveType leaveType) {

        LeaveTypeResponseDto dto = new LeaveTypeResponseDto();

        
        dto.setId(leaveType.getId());
        dto.setName(leaveType.getName());
        dto.setMaxDaysPerYear(leaveType.getMaxDaysPerYear());
        dto.setMaxCarryForwardDays(
                leaveType.getMaxCarryForwardDays() == null ? 0 : leaveType.getMaxCarryForwardDays());
        dto.setPaid(leaveType.isPaid());
        dto.setDescription(leaveType.getDescription());

        return dto;
    }

    
    public static void updateEntity(LeaveType leaveType,
                                    LeaveTypeRequestDto dto) {

        
        leaveType.setName(dto.getName());
        leaveType.setMaxDaysPerYear(dto.getMaxDaysPerYear());
        leaveType.setMaxCarryForwardDays(
                dto.getMaxCarryForwardDays() == null ? 0 : dto.getMaxCarryForwardDays());
        leaveType.setPaid(dto.getPaid());
        leaveType.setDescription(dto.getDescription());
    }

}
