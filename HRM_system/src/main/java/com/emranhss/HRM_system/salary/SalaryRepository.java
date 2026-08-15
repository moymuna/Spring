package com.emranhss.HRM_system.salary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary,Long> {
    
    List<Salary> findByEmployeeId(Long employeeId);

    
    Optional<Salary> findByEmployeeIdAndActiveTrue(Long employeeId);
}
