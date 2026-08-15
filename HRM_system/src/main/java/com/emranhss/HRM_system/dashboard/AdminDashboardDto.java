package com.emranhss.HRM_system.dashboard;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/** Everything the admin dashboard shows, in one call. */
@Data
public class AdminDashboardDto {

    private long totalEmployees;

    private long activeEmployees;

    private long inactiveEmployees;

    private long departments;

    private long onLeaveToday;

    private long newHiresThisMonth;

    private long pendingLeaveApprovals;

    private long pendingAdvanceApprovals;

    /** Active headcount per department, for the doughnut. */
    private Map<String, Long> headcountByDepartment = new LinkedHashMap<>();

    /** Today's attendance grouped by status, for the doughnut. */
    private Map<String, Long> attendanceToday = new LinkedHashMap<>();

    /** Month number (1-12) to headcount at the end of that month, for the trend line. */
    private Map<Integer, Long> headcountTrend = new LinkedHashMap<>();
}
