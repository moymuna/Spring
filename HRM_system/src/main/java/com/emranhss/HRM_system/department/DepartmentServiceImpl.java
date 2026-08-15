package com.emranhss.HRM_system.department;

import com.emranhss.HRM_system.exception.ConflictException;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.office.Office;
import com.emranhss.HRM_system.office.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final OfficeRepository officeRepository;

    
    @Override
    @Transactional
    public DepartmentResponseDto saveDepartment (DepartmentRequestDto dto) {

        
        if (departmentRepository.existsByDepartmentName(dto.getDepartmentName())) {
            throw new ConflictException("Department name already exists.");
        }

        
        if (departmentRepository.existsByCode(dto.getCode())) {
            throw new ConflictException("Department code already exists.");
        }

        
        Employee departmentHead = null;

        if (dto.getDepartmentHeadId() != null) {

            departmentHead = employeeRepository.findById(dto.getDepartmentHeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department head not found"));
        }

        


        Office office = officeRepository.findById(dto.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));


        
        Department department = DepartmentMapper.toEntity(
                dto,
                departmentHead,
                office
        );

        
        department = departmentRepository.save(department);

        
        return DepartmentMapper.toResponse(department);
    }

    
    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDto getDepartmentById(Long id) {

        
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        
        return DepartmentMapper.toResponse(department);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto) {

        
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        
        if (departmentRepository.existsByDepartmentName(dto.getDepartmentName())
                && !department.getDepartmentName().equals(dto.getDepartmentName())) {
            throw new ConflictException("Department name already exists.");
        }

        
        if (departmentRepository.existsByCode(dto.getCode())
                && !department.getCode().equals(dto.getCode())) {
            throw new ConflictException("Department code already exists.");
        }

        
        Employee departmentHead = null;
        if (dto.getDepartmentHeadId() != null) {
            departmentHead = employeeRepository.findById(dto.getDepartmentHeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department head not found"));
        }

        
        Office office = officeRepository.findById(dto.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));

        
        department.setDepartmentName(dto.getDepartmentName());
        department.setCode(dto.getCode());
        department.setDepartmentHead(departmentHead);
        department.setOffice(office);

        
        department = departmentRepository.save(department);

        return DepartmentMapper.toResponse(department);
    }

    
    @Override
    @Transactional
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        departmentRepository.delete(department);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(Long departmentId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        return department.getEmployees();
    }



@Override
@Transactional(readOnly = true)
public DepartmentResponseDto getDepartmentByName(String name) {

    Department department = departmentRepository.findByDepartmentName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with name: " + name));

    return DepartmentMapper.toResponse(department);
}


@Override
@Transactional(readOnly = true)
public DepartmentResponseDto getDepartmentByCode(String code) {

    Department department = departmentRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with code: " + code));

    return DepartmentMapper.toResponse(department);
}

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> searchDepartments(String keyword) {
        return departmentRepository.searchDepartments(keyword)
                .stream()
                .map(DepartmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getDepartmentCount() {
        return departmentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponseDto> getDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable)
                .map(DepartmentMapper::toResponse);
    }
}

