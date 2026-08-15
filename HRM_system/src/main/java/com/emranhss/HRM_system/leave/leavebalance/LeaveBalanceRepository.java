package com.emranhss.HRM_system.leave.leavebalance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.leave.leavetype.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance,Long> {

    List<LeaveBalance> findByEmployee(Employee employee);

    List<LeaveBalance> findByEmployeeAndYear(Employee employee, Integer year);

    List<LeaveBalance> findByLeaveType(LeaveType leaveType);

    List<LeaveBalance> findByYear(Integer year);

    Optional<LeaveBalance> findByEmployeeAndLeaveTypeAndYear(
            Employee employee,
            LeaveType leaveType,
            Integer year
    );

    @Query("""
            SELECT lb.leaveType.name, SUM(lb.totalEntitled), SUM(lb.used)
            FROM LeaveBalance lb
            WHERE lb.year = :year
            GROUP BY lb.leaveType.name
            """)
    List<Object[]> getUtilizationByLeaveType(Integer year);
}
