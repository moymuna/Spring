package com.emranhss.HRM_system.holiday;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayResponseDto {
    private Long id;

    private String name;

    private LocalDate date;

    private Boolean recurringYearly;

    private String description;
}
