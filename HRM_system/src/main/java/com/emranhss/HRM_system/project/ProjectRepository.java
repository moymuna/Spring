package com.emranhss.HRM_system.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Project> findProjectByEmployeeId(Long employeeId);


}
