package com.emranhss.HRM_system.attendance;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class AttendanceRequestDto {


        private LocalDate date;

        private LocalDateTime checkInTime;

        private LocalDateTime checkOutTime;

        private String status;

        private Long employeeId;

}
