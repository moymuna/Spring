package com.emranhss.HRM_system.applicant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    Optional<Applicant> findByEmail(String email);

    @Query("""
            SELECT a FROM Applicant a
            WHERE
            LOWER(a.name) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(a.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(a.skills) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Applicant> searchApplicants(String keyword);
}
