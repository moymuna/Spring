package com.emranhss.HRM_system.leave.leavebalance;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeaveBalanceService {
    
    LeaveBalanceResponseDto saveLeaveBalance(LeaveBalanceRequestDto dto);

    
    LeaveBalanceResponseDto getLeaveBalanceById(Long id);

    
    List<LeaveBalanceResponseDto> getAllLeaveBalances();

    
    LeaveBalanceResponseDto updateLeaveBalance(Long id,
                                               LeaveBalanceRequestDto dto);

    
    void deleteLeaveBalance(Long id);

    
    List<LeaveBalanceResponseDto> getLeaveBalancesByEmployee(Long employeeId);

    
    List<LeaveBalanceResponseDto> getLeaveBalancesByLeaveType(Long leaveTypeId);

    
    List<LeaveBalanceResponseDto> getLeaveBalancesByYear(Integer year);

    LeaveBalanceResponseDto getEmployeeLeaveBalance(Long employeeId, Long leaveTypeId, Integer year);

    
    List<LeaveUtilizationDto> getUtilizationByLeaveType(Integer year);

    void createBalancesForNewEmployee(com.emranhss.HRM_system.employee.Employee employee);
}
