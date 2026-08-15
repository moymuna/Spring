package com.emranhss.HRM_system.payslip;

import com.emranhss.HRM_system.enums.PayrollStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayslipRequestDto {
    private Integer month;

    private Integer year;

    private BigDecimal grossSalary;

    private BigDecimal totalDeductions;

    private BigDecimal netSalary;

    private Integer paidDays;

    private Integer lopDays;

    private PayrollStatus status;

    private LocalDateTime generatedAt;

    private LocalDateTime paidAt;
    private Long employeeId;

    private Long payrollId;
}
