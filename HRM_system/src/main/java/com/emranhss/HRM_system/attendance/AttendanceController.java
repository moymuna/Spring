package com.emranhss.HRM_system.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance/")
@RequiredArgsConstructor
public class AttendanceController {
    
    private final AttendanceService attendanceService;

    
    @PostMapping("/save")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#dto.employeeId)")
    public AttendanceResponseDto saveAttendance(
            @RequestBody AttendanceRequestDto dto) {

        return attendanceService.saveAttendance(dto);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isAttendanceOwnerOrNotEmployee(#id)")
    public AttendanceResponseDto getAttendanceById(
            @PathVariable Long id) {

        return attendanceService.getAttendanceById(id);
    }

    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<AttendanceResponseDto> getAllAttendance() {

        return attendanceService.getAllAttendance();
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public AttendanceResponseDto updateAttendance(
            @PathVariable Long id,
            @RequestBody AttendanceRequestDto dto) {

        return attendanceService.updateAttendance(id, dto);
    }

    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String deleteAttendance(
            @PathVariable Long id) {

        attendanceService.deleteAttendance(id);

        return "Attendance deleted successfully.";
    }


    @PostMapping("clock-in/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public AttendanceResponseDto clockIn(
            @PathVariable Long employeeId) {

        return attendanceService.clockIn(employeeId);
    }

    @PutMapping("clock-out/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public AttendanceResponseDto clockOut(
            @PathVariable Long employeeId) {

        return attendanceService.clockOut(employeeId);
    }

    @GetMapping("today/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public AttendanceResponseDto getTodayAttendance(
            @PathVariable Long employeeId) {

        return attendanceService.getTodayAttendance(employeeId);
    }


    /** An employee needs their own month to draw the attendance calendar. */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public List<AttendanceResponseDto> getAttendanceByMonth(

            @PathVariable Long employeeId,

            @RequestParam int year,

            @RequestParam int month) {

        return attendanceService.getAttendanceByMonth(
                employeeId,
                year,
                month
        );

    }

    @GetMapping("/stats/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public long todayAttendanceCount() {
        return attendanceService.getTodayAttendanceCount();
    }

    @GetMapping("/employee/{employeeId}/summary")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public AttendanceMonthlySummaryDto getMonthlySummary(
            @PathVariable Long employeeId,
            @RequestParam int year,
            @RequestParam int month) {
        return attendanceService.getMonthlySummary(employeeId, year, month);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<AttendanceResponseDto> search(@RequestParam String keyword) {
        return attendanceService.searchAttendance(keyword);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public Long count() {
        return attendanceService.getAttendanceCount();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public Page<AttendanceResponseDto> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceService.getAttendance(pageable);
    }

}
