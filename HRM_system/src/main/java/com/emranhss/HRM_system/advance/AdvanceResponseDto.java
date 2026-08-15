package com.emranhss.HRM_system.advance;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdvanceResponseDto {

    private Long id;

    private BigDecimal amount;

    private LocalDate requestDate;

    private LocalDate requiredByDate;

    private Integer installments;

    private BigDecimal monthlyDeduction;

    private BigDecimal recoveredAmount;

    private BigDecimal outstandingAmount;

    private String reason;

    private String status;

    private LocalDateTime decidedAt;

    private String rejectionReason;

    private Long employeeId;

    private String employeeName;

    private String employeeCode;
}
