package com.emranhss.HRM_system.leave.leavebalance;

import com.emranhss.HRM_system.enums.LeavesType;
import lombok.Data;

@Data
public class LeaveBalanceResponseDto {
    private Long id;

    private Integer year;

    private Double totalEntitled;

    private Double used;

    private Double remaining;

    private Long employeeId;

    private String employeeName;

    private Long leaveTypeId;

    private LeavesType leaveTypeName;
}
