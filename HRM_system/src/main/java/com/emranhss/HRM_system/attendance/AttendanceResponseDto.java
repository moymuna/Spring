package com.emranhss.HRM_system.attendance;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class AttendanceResponseDto {


        private Long id;

        private LocalDate date;

        private LocalDateTime checkInTime;

        private LocalDateTime checkOutTime;

        private Double workedHours;

        private String status;

        private Long employeeId;

        private String employeeName;

}
