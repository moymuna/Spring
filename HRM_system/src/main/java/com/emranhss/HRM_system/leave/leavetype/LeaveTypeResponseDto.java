package com.emranhss.HRM_system.leave.leavetype;

import com.emranhss.HRM_system.enums.LeavesType;
import lombok.Data;

@Data
public class LeaveTypeResponseDto {
    private Long id;

    private LeavesType name;

    private Integer maxDaysPerYear;

    private Integer maxCarryForwardDays;

    private Boolean paid;

    private String description;
}
