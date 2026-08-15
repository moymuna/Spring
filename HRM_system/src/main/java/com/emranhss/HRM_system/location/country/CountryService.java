package com.emranhss.HRM_system.location.country;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CountryService {
    
    CountryResponseDto saveCountry(CountryRequestDto dto);

    
    CountryResponseDto getCountryById(Long id);

    
    List<CountryResponseDto> getAllCountries();

    
    CountryResponseDto updateCountry(Long id,
                                     CountryRequestDto dto);

    
    void deleteCountry(Long id);

    
    CountryResponseDto getCountryByName(String countryName);

}
