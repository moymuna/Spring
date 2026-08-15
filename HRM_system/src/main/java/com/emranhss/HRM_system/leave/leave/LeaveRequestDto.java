package com.emranhss.HRM_system.leave.leave;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveRequestDto {

    private LocalDate startDate;

    private LocalDate endDate;

    private Double totalDays;

    private LocalDateTime decidedAt;

    private String reason;

    private String status;

    private String rejectionReason;

    private Long employeeId;

    private Long leaveTypeId;


}
