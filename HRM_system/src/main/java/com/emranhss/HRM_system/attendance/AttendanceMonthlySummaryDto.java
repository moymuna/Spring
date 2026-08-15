package com.emranhss.HRM_system.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceMonthlySummaryDto {

    private Long employeeId;
    private int year;
    private int month;

    private long presentDays;
    private long absentDays;
    private long halfDays;
    private long onLeaveDays;
    private long holidayDays;
    private long weekOffDays;
    private long workFromHomeDays;

    private double totalWorkedHours;
}
