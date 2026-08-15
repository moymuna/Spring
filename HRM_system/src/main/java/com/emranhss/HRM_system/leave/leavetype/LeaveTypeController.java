package com.emranhss.HRM_system.leave.leavetype;

import com.emranhss.HRM_system.enums.LeavesType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeaveTypeController {
    private final LeaveTypeService leaveTypeService;

    
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public LeaveTypeResponseDto saveLeaveType(
            @RequestBody LeaveTypeRequestDto dto) {

        return leaveTypeService.saveLeaveType(dto);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public LeaveTypeResponseDto getLeaveTypeById(
            @PathVariable Long id) {

        return leaveTypeService.getLeaveTypeById(id);
    }

    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public List<LeaveTypeResponseDto> getAllLeaveTypes() {

        return leaveTypeService.getAllLeaveTypes();
    }

    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public LeaveTypeResponseDto updateLeaveType(
            @PathVariable Long id,
            @RequestBody LeaveTypeRequestDto dto) {

        return leaveTypeService.updateLeaveType(id, dto);
    }

    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteLeaveType(
            @PathVariable Long id) {

        leaveTypeService.deleteLeaveType(id);

        return "Leave Type deleted successfully.";
    }

    
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public LeaveTypeResponseDto getLeaveTypeByName(
            @PathVariable LeavesType name) {

        return leaveTypeService.getLeaveTypeByName(name);
    }

}
