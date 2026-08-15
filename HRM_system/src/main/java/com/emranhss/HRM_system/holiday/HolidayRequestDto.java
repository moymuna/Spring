package com.emranhss.HRM_system.holiday;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayRequestDto {
    private String name;

    private LocalDate date;

    private Boolean recurringYearly;

    private String description;
}
