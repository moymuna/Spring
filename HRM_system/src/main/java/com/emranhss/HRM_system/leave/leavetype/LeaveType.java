package com.emranhss.HRM_system.leave.leavetype;

import com.emranhss.HRM_system.enums.LeavesType;
import com.emranhss.HRM_system.leave.leavebalance.LeaveBalance;
import com.emranhss.HRM_system.leave.leave.Leave;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "leaveType")
@AllArgsConstructor
@NoArgsConstructor
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Stored by name, not ordinal. As an ordinal, reordering the LeavesType enum
     * would silently change what every existing row means.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 60)
    private LeavesType name;

    @Column(nullable = false)
    private Integer maxDaysPerYear;

    /**
     * Unused days that may roll into next year's balance. 0 (or null on rows
     * created before this column existed) means the balance lapses at year end.
     */
    private Integer maxCarryForwardDays = 0;

    @Column(nullable = false)
    private boolean paid;

    @Column(length = 255)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "leaveType")
    private List<Leave> leaves;

    @JsonIgnore
    @OneToMany(mappedBy = "leaveType")
    private List<LeaveBalance> leaveBalances;
}
