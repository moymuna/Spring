package com.emranhss.HRM_system.interview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplication_Id(Long applicationId);

    List<Interview> findByInterviewer_Id(Long interviewerId);
}
