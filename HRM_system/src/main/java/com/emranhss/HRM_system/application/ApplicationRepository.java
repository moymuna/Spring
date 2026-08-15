package com.emranhss.HRM_system.application;

import com.emranhss.HRM_system.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByApplicant_Id(Long applicantId);

    List<Application> findByJobPost_Id(Long jobPostId);

    List<Application> findByStatus(ApplicationStatus status);

    @Query("""
            SELECT a FROM Application a
            WHERE
            LOWER(a.applicant.name) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(a.jobPost.title) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<Application> searchApplications(String keyword);
}
