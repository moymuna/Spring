package com.emranhss.HRM_system.dashboard;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BirthdayDto {

    private Long employeeId;

    private String employeeName;

    private String employeeCode;

    private String image;

    /** The next occurrence of the birthday, not the year of birth. */
    private LocalDate nextBirthday;

    private int daysUntil;
}
