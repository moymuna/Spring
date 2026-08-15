package com.emranhss.HRM_system.leave.leavebalance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.leave.leavetype.LeaveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "leaveBalance")
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Double totalEntitled;


    @Column(nullable = false)
    private Double used = 0.0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Transient
    public Double getRemaining() {
        return totalEntitled - used;
    }
}
