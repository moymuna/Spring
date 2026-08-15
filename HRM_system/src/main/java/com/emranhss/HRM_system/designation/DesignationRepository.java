package com.emranhss.HRM_system.designation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignationRepository extends JpaRepository<Designation,Long> {
    List<Designation> findByDepartment_Id(Long departmentId);

    @Query("""
            SELECT d FROM Designation d
            WHERE
            LOWER(d.title) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(d.level) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Designation> searchDesignations(String keyword);
}
