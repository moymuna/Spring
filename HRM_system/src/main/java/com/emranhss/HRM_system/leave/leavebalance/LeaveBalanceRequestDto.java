package com.emranhss.HRM_system.leave.leavebalance;

import lombok.Data;

@Data
public class LeaveBalanceRequestDto {
    private Integer year;

    private Double totalEntitled;

    private Double used;

    private Long employeeId;

    private Long leaveTypeId;
}
