package com.emranhss.HRM_system.salary;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SalaryService {

    
    SalaryResponseDto createSalary(SalaryRequestDto dto);

    
    List<SalaryResponseDto> getAllSalaries();


    SalaryResponseDto getSalaryById(Long id);


    SalaryResponseDto getSalaryByEmployee(Long employeeId);


    List<SalaryResponseDto> getSalaryHistoryByEmployee(Long employeeId);


    SalaryResponseDto updateSalary(Long id,
                                   SalaryRequestDto dto);

    
    void deleteSalary(Long id);
}
