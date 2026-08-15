package com.emranhss.HRM_system.leave.leave;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {
    List<Leave> findByEmployeeId(Long employeeId);

    List<Leave> findByLeaveTypeId(Long leaveTypeId);

    List<Leave> findByStatus(LeaveStatus status);

    List<Leave> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<Leave> findByEmployee(Employee employee);

    @Query("""
            SELECT l FROM Leave l
            WHERE
            LOWER(l.employee.user.fullName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(l.employee.employeeCode) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(l.reason) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Leave> searchLeaves(String keyword);

    @Query("""
            SELECT l FROM Leave l
            WHERE l.employee.id = :employeeId
            AND l.status = com.emranhss.HRM_system.enums.LeaveStatus.APPROVED
            AND :date BETWEEN l.startDate AND l.endDate
            """)
    Optional<Leave> findApprovedLeaveCoveringDate(Long employeeId, LocalDate date);

    /**
     * Approved leave on an unpaid leave type that overlaps the given window.
     * Payroll deducts these days; paid leave types are left alone.
     */
    @Query("""
            SELECT l FROM Leave l
            WHERE l.employee.id = :employeeId
            AND l.status = com.emranhss.HRM_system.enums.LeaveStatus.APPROVED
            AND l.leaveType.paid = false
            AND l.startDate <= :windowEnd
            AND l.endDate >= :windowStart
            """)
    List<Leave> findUnpaidApprovedLeaves(Long employeeId, LocalDate windowStart, LocalDate windowEnd);
}
