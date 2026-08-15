package com.emranhss.HRM_system.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /** One call for the whole admin dashboard, rather than a request per tile. */
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<AdminDashboardDto> adminDashboard(
            @RequestParam(required = false) Integer year) {

        int targetYear = year != null ? year : LocalDate.now().getYear();

        return ResponseEntity.ok(dashboardService.getAdminDashboard(targetYear));
    }

    @GetMapping("/birthdays")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<BirthdayDto>> upcomingBirthdays(
            @RequestParam(defaultValue = "30") int withinDays) {

        return ResponseEntity.ok(dashboardService.getUpcomingBirthdays(withinDays));
    }
}
