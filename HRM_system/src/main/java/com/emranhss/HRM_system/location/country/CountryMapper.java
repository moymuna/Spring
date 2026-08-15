package com.emranhss.HRM_system.location.country;

public class CountryMapper {
    
    public static Country toEntity(CountryRequestDto dto) {

        Country country = new Country();

        
        country.setCountryName(dto.getCountryName());
        country.setCode(dto.getCode());
        country.setPhoneCode(dto.getPhoneCode());

        return country;
    }

    
    public static CountryResponseDto toResponse(Country country) {

        CountryResponseDto dto = new CountryResponseDto();

        dto.setId(country.getId());
        dto.setCountryName(country.getCountryName());
        dto.setCode(country.getCode());
        dto.setPhoneCode(country.getPhoneCode());

        return dto;
    }

    
    public static void updateEntity(Country country,
                                    CountryRequestDto dto) {

        country.setCountryName(dto.getCountryName());
        country.setCode(dto.getCode());
        country.setPhoneCode(dto.getPhoneCode());
    }
}
