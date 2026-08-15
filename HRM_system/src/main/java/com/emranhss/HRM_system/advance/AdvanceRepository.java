package com.emranhss.HRM_system.advance;

import com.emranhss.HRM_system.enums.AdvanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvanceRepository extends JpaRepository<Advance, Long> {

    List<Advance> findByEmployeeIdOrderByRequestDateDesc(Long employeeId);

    List<Advance> findByStatusOrderByRequestDateDesc(AdvanceStatus status);

    List<Advance> findByEmployeeIdAndStatus(Long employeeId, AdvanceStatus status);

    long countByStatus(AdvanceStatus status);

    @Query("""
            SELECT a FROM Advance a
            WHERE
            LOWER(a.employee.user.fullName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(a.employee.employeeCode) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(a.reason) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Advance> searchAdvances(String keyword);
}
