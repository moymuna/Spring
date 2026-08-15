package com.emranhss.HRM_system.leave.leave;

import com.emranhss.HRM_system.enums.LeavesType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveResponseDto {
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double totalDays;

    private String reason;

    private String status;

    private LocalDateTime decidedAt;

    private String rejectionReason;

    private Long employeeId;

    private String employeeName;

    private Long leaveTypeId;

    private LeavesType leaveTypeName;
}
