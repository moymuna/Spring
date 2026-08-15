package com.emranhss.HRM_system.salarygrade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryGradeRepository extends JpaRepository<SalaryGrade, Long> {

    Optional<SalaryGrade> findByGradeNumber(Integer gradeNumber);

    boolean existsByGradeNumber(Integer gradeNumber);

    List<SalaryGrade> findAllByOrderByGradeNumberAsc();

    List<SalaryGrade> findByActiveTrueOrderByGradeNumberAsc();
}
