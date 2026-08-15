package com.emranhss.HRM_system.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Long> {

    Optional<Payroll> findByEmployeeIdAndMonthAndYear(Long employeeId, Integer month, Integer year);

    List<Payroll> findByEmployeeIdOrderByYearDescMonthDesc(Long employeeId);

    @Query("""
            SELECT p FROM Payroll p
            WHERE
            LOWER(p.employee.user.fullName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(p.employee.employeeCode) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Payroll> searchPayrolls(String keyword);

    @Query("""
            SELECT p.month, SUM(p.netSalary)
            FROM Payroll p
            WHERE p.year = :year
            GROUP BY p.month
            ORDER BY p.month
            """)
    List<Object[]> getMonthlyCostTrend(Integer year);
}
