package com.emranhss.HRM_system.training;

import com.emranhss.HRM_system.enums.TrainingStatus;
import lombok.Data;

import java.time.LocalDate;


@Data
public class TrainingResponseDto {
    private Long id;

    private String trainingTitle;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long employeeId;

    private String employeeName;
    private Long departmentId;

    private String departmentName;

    private TrainingStatus status;

    private String rejectionReason;
}
