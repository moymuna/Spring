package com.emranhss.HRM_system.location.division;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DivisionService {
    
    DivisionResponseDto saveDivision(DivisionRequestDto dto);

    
    DivisionResponseDto getDivisionById(Long id);

    
    List<DivisionResponseDto> getAllDivisions();

    
    DivisionResponseDto updateDivision(Long id,
                                       DivisionRequestDto dto);

    
    void deleteDivision(Long id);

    
    List<DivisionResponseDto> getDivisionsByCountryId(Long countryId);

    
    List<DivisionResponseDto> getDivisionsByCountryName(String countryName);
}
