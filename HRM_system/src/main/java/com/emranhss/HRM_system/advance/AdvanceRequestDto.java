package com.emranhss.HRM_system.advance;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdvanceRequestDto {

    private BigDecimal amount;

    private LocalDate requestDate;

    private LocalDate requiredByDate;

    private Integer installments;

    private String reason;

    private Long employeeId;
}
