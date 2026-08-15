package com.emranhss.HRM_system.training;

import lombok.Data;

import java.time.LocalDate;


@Data
public class TrainingRequestDto {

    private String trainingTitle;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long employeeId;
    private Long departmentId;
}
