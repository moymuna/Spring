package com.emranhss.HRM_system.location.country;

import com.emranhss.HRM_system.exception.ConflictException;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    
    private final CountryRepository countryRepository;

    
    @Override
    @Transactional
    public CountryResponseDto saveCountry(CountryRequestDto dto) {

        
        if (countryRepository.existsByCountryName(dto.getCountryName())) {
            throw new ConflictException("Country name already exists.");
        }

        
        if (countryRepository.existsByCode(dto.getCode())) {
            throw new ConflictException("Country code already exists.");
        }

        
        Country country = CountryMapper.toEntity(dto);

        
        Country savedCountry = countryRepository.save(country);

        
        return CountryMapper.toResponse(savedCountry);
    }

    
    @Override
    @Transactional(readOnly = true)
    public CountryResponseDto getCountryById(Long id) {

        Country country = countryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found."));

        return CountryMapper.toResponse(country);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<CountryResponseDto> getAllCountries() {

        return countryRepository.findAll()
                .stream()
                .map(CountryMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional
    public CountryResponseDto updateCountry(Long id,
                                            CountryRequestDto dto) {

        
        Country country = countryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found."));

        
        countryRepository.findByCountryName(dto.getCountryName())
                .ifPresent(existing -> {
                    if (existing.getId() != country.getId()) {
                        throw new ConflictException("Country name already exists.");
                    }
                });

        
        countryRepository.findByCode(dto.getCode())
                .ifPresent(existing -> {
                    if (existing.getId() != country.getId()) {
                        throw new ConflictException("Country code already exists.");
                    }
                });

        
        CountryMapper.updateEntity(country, dto);

        
        Country updatedCountry = countryRepository.save(country);

        return CountryMapper.toResponse(updatedCountry);
    }

    
    @Override
    @Transactional
    public void deleteCountry(Long id) {

        Country country = countryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found."));

        countryRepository.delete(country);
    }

    
    @Override
    @Transactional(readOnly = true)
    public CountryResponseDto getCountryByName(String countryName) {

        Country country = countryRepository.findByCountryName(countryName)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found."));

        return CountryMapper.toResponse(country);
    }
}
