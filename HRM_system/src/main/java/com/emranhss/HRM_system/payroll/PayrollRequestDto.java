package com.emranhss.HRM_system.payroll;

import com.emranhss.HRM_system.enums.PayrollStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayrollRequestDto {
    private Integer month;

    private Integer year;

    private BigDecimal grossSalary;

    private BigDecimal totalDeductions;

    private BigDecimal netSalary;

    private Integer paidDays;

    private Integer lopDays;

    private PayrollStatus status;

    private Long employeeId;

    private LocalDateTime generatedAt;

    private LocalDateTime paidAt;
}
