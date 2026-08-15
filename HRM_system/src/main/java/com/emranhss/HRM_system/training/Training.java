package com.emranhss.HRM_system.training;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.enums.TrainingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trainings")
public class Training {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainingTitle;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    private Department department;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrainingStatus status = TrainingStatus.PENDING;

    
    private String rejectionReason;

}
