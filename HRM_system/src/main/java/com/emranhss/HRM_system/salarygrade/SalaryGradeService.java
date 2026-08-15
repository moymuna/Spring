package com.emranhss.HRM_system.salarygrade;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SalaryGradeService {

    SalaryGradeResponseDto createGrade(SalaryGradeRequestDto dto);

    List<SalaryGradeResponseDto> getAllGrades();

    List<SalaryGradeResponseDto> getActiveGrades();

    SalaryGradeResponseDto getGradeById(Long id);

    SalaryGradeResponseDto getGradeByNumber(Integer gradeNumber);

    SalaryGradeResponseDto updateGrade(Long id, SalaryGradeRequestDto dto);

    void deleteGrade(Long id);
}
