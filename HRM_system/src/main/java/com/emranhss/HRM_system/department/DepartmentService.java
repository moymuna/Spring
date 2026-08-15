package com.emranhss.HRM_system.department;

import com.emranhss.HRM_system.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DepartmentService {

    
    DepartmentResponseDto saveDepartment(DepartmentRequestDto dto);

    
    DepartmentResponseDto getDepartmentById(Long id);

    
    List<DepartmentResponseDto> getAllDepartments();

    
    DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto);

    
    void deleteDepartment(Long id);

    List<Employee> getEmployeesByDepartment(Long departmentId);

    
    DepartmentResponseDto getDepartmentByName(String departmentName);

    
    DepartmentResponseDto getDepartmentByCode(String code);

    List<DepartmentResponseDto> searchDepartments(String keyword);

    long getDepartmentCount();

    Page<DepartmentResponseDto> getDepartments(Pageable pageable);

}
