package com.emranhss.HRM_system.jobpost;

import com.emranhss.HRM_system.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    List<JobPost> findByStatus(JobStatus status);

    List<JobPost> findByDepartment_Id(Long departmentId);

    @Query("""
            SELECT j FROM JobPost j
            WHERE
            LOWER(j.title) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(j.location) LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    List<JobPost> searchJobPosts(String keyword);
}
