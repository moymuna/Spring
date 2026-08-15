package com.emranhss.HRM_system.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    List<Attendance> findByEmployeeIdAndDateBetween(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
            SELECT a FROM Attendance a
            WHERE
            LOWER(a.employee.user.fullName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(a.employee.employeeCode) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Attendance> searchAttendance(String keyword);

    long countByDate(LocalDate date);

    List<Attendance> findByDate(LocalDate date);
}
