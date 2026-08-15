package com.emranhss.HRM_system.leave.leavebalance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.leave.leavetype.LeaveType;

public class LeaveBalanceMapper {

    
    public static LeaveBalance toEntity(LeaveBalanceRequestDto dto,
                                        Employee employee,
                                        LeaveType leaveType) {

        LeaveBalance leaveBalance = new LeaveBalance();

        
        leaveBalance.setYear(dto.getYear());
        leaveBalance.setTotalEntitled(dto.getTotalEntitled());
        leaveBalance.setUsed(dto.getUsed());

        
        leaveBalance.setEmployee(employee);
        leaveBalance.setLeaveType(leaveType);

        return leaveBalance;
    }

    
    public static LeaveBalanceResponseDto toResponse(LeaveBalance leaveBalance) {

        LeaveBalanceResponseDto dto = new LeaveBalanceResponseDto();

        
        dto.setId(leaveBalance.getId());
        dto.setYear(leaveBalance.getYear());
        dto.setTotalEntitled(leaveBalance.getTotalEntitled());
        dto.setUsed(leaveBalance.getUsed());

        
        dto.setRemaining(leaveBalance.getRemaining());

        
        if (leaveBalance.getEmployee() != null) {

            dto.setEmployeeId(leaveBalance.getEmployee().getId());
            dto.setEmployeeName(leaveBalance.getEmployee().getUser().getFullName());
        }

        
        if (leaveBalance.getLeaveType() != null) {

            dto.setLeaveTypeId(leaveBalance.getLeaveType().getId());
            dto.setLeaveTypeName(leaveBalance.getLeaveType().getName());
        }

        return dto;
    }

    
    public static void updateEntity(LeaveBalance leaveBalance,
                                    LeaveBalanceRequestDto dto,
                                    Employee employee,
                                    LeaveType leaveType) {

        
        leaveBalance.setYear(dto.getYear());
        leaveBalance.setTotalEntitled(dto.getTotalEntitled());
        leaveBalance.setUsed(dto.getUsed());

        
        leaveBalance.setEmployee(employee);
        leaveBalance.setLeaveType(leaveType);
    }
}
