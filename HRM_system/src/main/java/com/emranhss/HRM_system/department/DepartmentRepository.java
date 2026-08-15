package com.emranhss.HRM_system.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {

    Optional<Department> findByDepartmentName(String departmentName);

    Optional<Department> findByCode(String code);

    boolean existsByDepartmentName(String departmentName);

    boolean existsByCode(String code);

    @Query("""
            SELECT d FROM Department d
            WHERE
            LOWER(d.departmentName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(d.code) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Department> searchDepartments(String keyword);
}
