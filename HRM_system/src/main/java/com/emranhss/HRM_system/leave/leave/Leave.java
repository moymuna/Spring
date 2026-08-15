package com.emranhss.HRM_system.leave.leave;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.LeaveStatus;
import com.emranhss.HRM_system.leave.leavetype.LeaveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "leaveHistory")
@AllArgsConstructor
@NoArgsConstructor
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private   LocalDate startDate;

    @Column(nullable = false)
    private LocalDate  endDate;

    @Column(nullable = false)
    private Double totalDays;
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LeaveStatus status;
    private LocalDateTime decidedAt;

    @Column(length = 500)
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;
}
