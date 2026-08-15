package com.emranhss.HRM_system.location.country;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/country")
@RequiredArgsConstructor
public class CountryController {
    
    private final CountryService countryService;

    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CountryResponseDto saveCountry(
            @RequestBody CountryRequestDto dto) {

        return countryService.saveCountry(dto);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CountryResponseDto getCountryById(
            @PathVariable Long id) {

        return countryService.getCountryById(id);
    }

    
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<CountryResponseDto> getAllCountries() {

        return countryService.getAllCountries();
    }

    
    @GetMapping("/name/{countryName}")
    @PreAuthorize("isAuthenticated()")
    public CountryResponseDto getCountryByName(
            @PathVariable String countryName) {

        return countryService.getCountryByName(countryName);
    }

    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CountryResponseDto updateCountry(
            @PathVariable Long id,
            @RequestBody CountryRequestDto dto) {

        return countryService.updateCountry(id, dto);
    }

    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCountry(
            @PathVariable Long id) {

        countryService.deleteCountry(id);

        return "Country deleted successfully.";
    }

}
