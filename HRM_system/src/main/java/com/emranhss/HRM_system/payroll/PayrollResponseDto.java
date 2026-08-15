package com.emranhss.HRM_system.payroll;

import com.emranhss.HRM_system.enums.PayrollStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayrollResponseDto {
    private Long id;

    private Integer month;

    private Integer year;

    private BigDecimal grossSalary;

    private BigDecimal totalDeductions;

    private BigDecimal netSalary;

    private Integer paidDays;

    private Integer lopDays;

    private Integer unpaidLeaveDays;

    private BigDecimal leaveDeduction;

    private BigDecimal advanceDeduction;

    private PayrollStatus status;

    private LocalDateTime generatedAt;

    private LocalDateTime paidAt;

    private Long employeeId;

    private String employeeName;
}
