package com.emranhss.HRM_system.leave.leave;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    
    private final LeaveService leaveService;

    
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER') or @employeeSecurity.isOwnerOrNotEmployee(#dto.employeeId)")
    public LeaveResponseDto saveLeave(
            @RequestBody LeaveRequestDto dto) {

        return leaveService.saveLeave(dto);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isLeaveOwnerOrNotEmployee(#id)")
    public LeaveResponseDto getLeaveById(
            @PathVariable Long id) {

        return leaveService.getLeaveById(id);
    }

    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<LeaveResponseDto> getAllLeaves() {

        return leaveService.getAllLeaves();
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public LeaveResponseDto updateLeave(
            @PathVariable Long id,
            @RequestBody LeaveRequestDto dto) {

        return leaveService.updateLeave(id, dto);
    }

    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String deleteLeave(
            @PathVariable Long id) {

        leaveService.deleteLeave(id);

        return "Leave deleted successfully.";
    }

    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public List<LeaveResponseDto> getLeavesByEmployee(
            @PathVariable Long employeeId) {

        return leaveService.getLeavesByEmployee(employeeId);
    }

    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<LeaveResponseDto> getLeavesByStatus(
            @PathVariable String status) {

        return leaveService.getLeavesByStatus(status);
    }

    
    @GetMapping("/between")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<LeaveResponseDto> getLeavesBetweenDates(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return leaveService.getLeavesBetweenDates(startDate, endDate);
    }
    
    @PutMapping("/approve/{leaveId}")
    @PreAuthorize("@employeeSecurity.canDecideOnLeave(#leaveId)")
    public LeaveResponseDto approveLeave(
            @PathVariable Long leaveId) {

        return leaveService.approveLeave(leaveId);
    }
    
    @PutMapping("/reject/{leaveId}")
    @PreAuthorize("@employeeSecurity.canDecideOnLeave(#leaveId)")
    public LeaveResponseDto rejectLeave(
            @PathVariable Long leaveId,
            @RequestParam String rejectionReason) {

        return leaveService.rejectLeave(leaveId, rejectionReason);
    }

    /** Owner may cancel their own leave; HR/Admin/Manager may cancel anyone's. */
    @PutMapping("/cancel/{leaveId}")
    @PreAuthorize("@employeeSecurity.isLeaveOwnerOrNotEmployee(#leaveId)")
    public LeaveResponseDto cancelLeave(
            @PathVariable Long leaveId) {

        return leaveService.cancelLeave(leaveId);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<LeaveResponseDto> search(@RequestParam String keyword) {
        return leaveService.searchLeaves(keyword);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public Long count() {
        return leaveService.getLeaveCount();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public Page<LeaveResponseDto> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return leaveService.getLeaves(pageable);
    }

}
