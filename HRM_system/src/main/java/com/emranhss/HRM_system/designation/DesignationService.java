package com.emranhss.HRM_system.designation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DesignationService {
    DesignationResponseDto createDesignation(DesignationRequestDto dto);

    DesignationResponseDto getDesignationById(Long id);

    List<DesignationResponseDto> getAllDesignations();

    DesignationResponseDto updateDesignation(Long id, DesignationRequestDto dto);

    void deleteDesignation(Long id);

    List<DesignationResponseDto> getByDepartmentId(Long departmentId);

    List<DesignationResponseDto> searchDesignations(String keyword);

    long getDesignationCount();

    Page<DesignationResponseDto> getDesignations(Pageable pageable);
}
