package com.emranhss.HRM_system.designation;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.department.Department;
import com.emranhss.HRM_system.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignationServiceImpl implements DesignationService {
    private final DesignationRepository designationRepository;
    private final DepartmentRepository departmentRepository;

    
    @Override
    @Transactional
    public DesignationResponseDto createDesignation(DesignationRequestDto dto) {

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Designation designation = DesignationMapper.toEntity(dto, department);

        designation = designationRepository.save(designation);

        return DesignationMapper.toResponse(designation);
    }

    
    @Override
    @Transactional(readOnly = true)
    public DesignationResponseDto getDesignationById(Long id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));

        return DesignationMapper.toResponse(designation);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DesignationResponseDto> getAllDesignations() {

        return designationRepository.findAll()
                .stream()
                .map(DesignationMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional
    public DesignationResponseDto updateDesignation(Long id, DesignationRequestDto dto) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        DesignationMapper.updateEntity(designation, dto, department);

        designation = designationRepository.save(designation);

        return DesignationMapper.toResponse(designation);
    }

    
    @Override
    @Transactional
    public void deleteDesignation(Long id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));

        designationRepository.delete(designation);
    }

    

    @Override
    @Transactional(readOnly = true)
    public List<DesignationResponseDto> getByDepartmentId(Long departmentId) {

        return designationRepository.findByDepartment_Id(departmentId)
                .stream()
                .map(DesignationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignationResponseDto> searchDesignations(String keyword) {
        return designationRepository.searchDesignations(keyword)
                .stream()
                .map(DesignationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getDesignationCount() {
        return designationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DesignationResponseDto> getDesignations(Pageable pageable) {
        return designationRepository.findAll(pageable)
                .map(DesignationMapper::toResponse);
    }

}
