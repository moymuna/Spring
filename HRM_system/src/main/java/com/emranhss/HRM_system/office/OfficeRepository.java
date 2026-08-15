package com.emranhss.HRM_system.office;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office,Long> {

    @Query("""
            SELECT o FROM Office o
            WHERE
            LOWER(o.officeName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(o.officeCode) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Office> searchOffices(String keyword);
}
