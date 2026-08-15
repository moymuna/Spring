package com.emranhss.HRM_system.location.division;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.location.country.Country;
import com.emranhss.HRM_system.location.country.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DivisionServiceImpl implements DivisionService {
    private final DivisionRepository divisionRepository;
    private final CountryRepository countryRepository;

    public DivisionServiceImpl(DivisionRepository divisionRepository,
                               CountryRepository countryRepository) {
        this.divisionRepository = divisionRepository;
        this.countryRepository = countryRepository;
    }

    
    @Override
    @Transactional
    public DivisionResponseDto saveDivision(DivisionRequestDto dto) {

        
        Country country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found with ID : " + dto.getCountryId()));

        
        Division division = DivisionMapper.toEntity(dto, country);

        
        division = divisionRepository.save(division);

        return DivisionMapper.toResponse(division);
    }

    
    @Override
    @Transactional(readOnly = true)
    public DivisionResponseDto getDivisionById(Long id) {

        Division division = divisionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Division not found with ID : " + id));

        return DivisionMapper.toResponse(division);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DivisionResponseDto> getAllDivisions() {

        return divisionRepository.findAll()
                .stream()
                .map(DivisionMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional
    public DivisionResponseDto updateDivision(Long id,
                                              DivisionRequestDto dto) {

        
        Division division = divisionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Division not found with ID : " + id));

        
        Country country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found with ID : " + dto.getCountryId()));

        
        DivisionMapper.updateEntity(division, dto, country);

        
        division = divisionRepository.save(division);

        return DivisionMapper.toResponse(division);
    }

    
    @Override
    @Transactional
    public void deleteDivision(Long id) {

        Division division = divisionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Division not found with ID : " + id));

        divisionRepository.delete(division);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DivisionResponseDto> getDivisionsByCountryId(Long countryId) {

        return divisionRepository.findByCountryId(countryId)
                .stream()
                .map(DivisionMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DivisionResponseDto> getDivisionsByCountryName(String countryName) {

        return divisionRepository.findByCountryCountryName(countryName)
                .stream()
                .map(DivisionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
