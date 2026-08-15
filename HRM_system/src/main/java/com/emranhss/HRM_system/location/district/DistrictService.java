package com.emranhss.HRM_system.location.district;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DistrictService {
    
    DistrictResponseDto createDistrict(DistrictRequestDto requestDto);

    
    List<DistrictResponseDto> getAllDistricts();

    
    DistrictResponseDto getDistrictById(Long id);

    
    DistrictResponseDto updateDistrict(Long id,
                                       DistrictRequestDto requestDto);

    
    void deleteDistrict(Long id);

    
    List<DistrictResponseDto> getDistrictsByDivisionId(Long divisionId);

    
    List<DistrictResponseDto> getDistrictsByDivisionName(String divisionName);
}
