package com.emranhss.HRM_system.holiday;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holiday/")
@RequiredArgsConstructor
public class HolidayController {
    
    private final HolidayService holidayService;

    
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public HolidayResponseDto saveHoliday(
            @RequestBody HolidayRequestDto dto) {

        return holidayService.saveHoliday(dto);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public HolidayResponseDto getHolidayById(
            @PathVariable Long id) {

        return holidayService.getHolidayById(id);
    }

    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public List<HolidayResponseDto> getAllHolidays() {

        return holidayService.getAllHolidays();
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public HolidayResponseDto updateHoliday(
            @PathVariable Long id,
            @RequestBody HolidayRequestDto dto) {

        return holidayService.updateHoliday(id, dto);
    }

    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteHoliday(
            @PathVariable Long id) {

        holidayService.deleteHoliday(id);

        return "Holiday deleted successfully.";
    }


    /** Feeds the "Upcoming Holidays" panel on the employee dashboard. */
    @GetMapping("/upcoming")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public List<HolidayResponseDto> getUpcomingHolidays(
            @RequestParam(defaultValue = "5") int limit) {

        return holidayService.getUpcomingHolidays(limit);
    }


    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public HolidayResponseDto getHolidayByName(
            @PathVariable String name) {

        return holidayService.getHolidayByName(name);
    }

    
    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public HolidayResponseDto getHolidayByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return holidayService.getHolidayByDate(date);
    }


}
