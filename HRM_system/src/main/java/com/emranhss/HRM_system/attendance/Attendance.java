package com.emranhss.HRM_system.attendance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "Attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Transient
    public Double getWorkedHours() {
        if (checkInTime == null || checkOutTime == null) return null;
        return (checkOutTime.toEpochSecond(java.time.ZoneOffset.UTC)
                - checkInTime.toEpochSecond(java.time.ZoneOffset.UTC)) / 3600.0;
    }

}
