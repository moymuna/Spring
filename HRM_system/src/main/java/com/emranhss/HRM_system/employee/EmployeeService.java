package com.emranhss.HRM_system.employee;

import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.enums.EmploymentType;
import com.emranhss.HRM_system.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

public interface EmployeeService {

    
    EmployeeResponseDto saveEmployee(EmployeeRequestDto dto, MultipartFile image);


    EmployeeResponseDto hireApplicant(Long applicationId, HireRequestDto dto);

    
    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto, MultipartFile image);

    
    void deleteEmployee(Long id);

    
    EmployeeResponseDto getEmployeeById(Long id);

    
    List<EmployeeResponseDto> getAllEmployees();

    
    EmployeeResponseDto getEmployeeByCode(String employeeCode);

    
    EmployeeResponseDto getEmployeeByEmail(String email);

    
    EmployeeResponseDto getEmployeeByUserId(Long userId);

    
    List<EmployeeResponseDto> getEmployeesByDepartment(Long departmentId);

    
    List<EmployeeResponseDto> getEmployeesByDesignation(Long designationId);

    
    List<EmployeeResponseDto> getEmployeesByOffice(Long officeId);

    
    List<EmployeeResponseDto> getEmployeesByManager(Long managerId);

    
    List<EmployeeResponseDto> getEmployeesByStatus(EmployeeStatus status);

    
    List<EmployeeResponseDto> getEmployeesByEmploymentType(EmploymentType employmentType);

    
    List<EmployeeResponseDto> getEmployeesByGender(Gender gender);


    List<EmployeeResponseDto> searchEmployees(String keyword);

    long getEmployeeCount();

    long getActiveEmployeeCount();

    long getInactiveEmployeeCount();

    Page<EmployeeResponseDto> getEmployees(Pageable pageable);

    Map<String, Long> getActiveHeadcountByDepartment();
}