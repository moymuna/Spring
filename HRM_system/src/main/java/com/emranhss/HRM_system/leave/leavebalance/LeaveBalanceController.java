package com.emranhss.HRM_system.leave.leavebalance;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balance")
@RequiredArgsConstructor
public class LeaveBalanceController {

    
    private final LeaveBalanceService leaveBalanceService;
    private final LeaveBalanceAccrualJob leaveBalanceAccrualJob;

    
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public LeaveBalanceResponseDto saveLeaveBalance(
            @RequestBody LeaveBalanceRequestDto dto) {

        return leaveBalanceService.saveLeaveBalance(dto);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isLeaveBalanceOwnerOrNotEmployee(#id)")
    public LeaveBalanceResponseDto getLeaveBalanceById(
            @PathVariable Long id) {

        return leaveBalanceService.getLeaveBalanceById(id);
    }

    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<LeaveBalanceResponseDto> getAllLeaveBalances() {

        return leaveBalanceService.getAllLeaveBalances();
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public LeaveBalanceResponseDto updateLeaveBalance(
            @PathVariable Long id,
            @RequestBody LeaveBalanceRequestDto dto) {

        return leaveBalanceService.updateLeaveBalance(id, dto);
    }

    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteLeaveBalance(
            @PathVariable Long id) {

        leaveBalanceService.deleteLeaveBalance(id);

        return "Leave balance deleted successfully.";
    }

    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public List<LeaveBalanceResponseDto> getLeaveBalancesByEmployee(
            @PathVariable Long employeeId) {

        return leaveBalanceService.getLeaveBalancesByEmployee(employeeId);
    }

    
    @GetMapping("/leave-type/{leaveTypeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<LeaveBalanceResponseDto> getLeaveBalancesByLeaveType(
            @PathVariable Long leaveTypeId) {

        return leaveBalanceService.getLeaveBalancesByLeaveType(leaveTypeId);
    }

    
    @GetMapping("/year/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<LeaveBalanceResponseDto> getLeaveBalancesByYear(
            @PathVariable Integer year) {

        return leaveBalanceService.getLeaveBalancesByYear(year);
    }

    
    @GetMapping("/employee/{employeeId}/leave-type/{leaveTypeId}/year/{year}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public LeaveBalanceResponseDto getEmployeeLeaveBalance(
            @PathVariable Long employeeId,
            @PathVariable Long leaveTypeId,
            @PathVariable Integer year) {

        return leaveBalanceService.getEmployeeLeaveBalance(
                employeeId,
                leaveTypeId,
                year);
    }

    
    @PostMapping("/accrue/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public String accrue(@PathVariable int year) {
        int created = leaveBalanceAccrualJob.run(year);
        return created + " leave balance record(s) created for " + year + ".";
    }

    
    @GetMapping("/stats/utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<LeaveUtilizationDto> utilization(@RequestParam int year) {
        return leaveBalanceService.getUtilizationByLeaveType(year);
    }

}
