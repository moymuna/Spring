package com.emranhss.HRM_system.leave.leavebalance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveUtilizationDto {
    private String leaveTypeName;
    private Double totalEntitled;
    private Double totalUsed;
    private Double remaining;
}
