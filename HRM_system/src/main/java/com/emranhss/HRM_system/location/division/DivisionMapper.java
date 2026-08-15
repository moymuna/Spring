package com.emranhss.HRM_system.location.division;

import com.emranhss.HRM_system.location.country.Country;

public class DivisionMapper {

    
    public static Division toEntity(DivisionRequestDto dto,
                                    Country country) {

        Division division = new Division();

        
        division.setName(dto.getName());
        division.setNameBN(dto.getNameBN());

        
        division.setCountry(country);

        return division;
    }

    
    public static DivisionResponseDto toResponse(Division division) {

        DivisionResponseDto dto = new DivisionResponseDto();

        dto.setId(division.getId());
        dto.setName(division.getName());
        dto.setNameBN(division.getNameBN());

        if (division.getCountry() != null) {

            dto.setCountryId(division.getCountry().getId());
            dto.setCountryName(division.getCountry().getCountryName());
        }

        return dto;
    }

    
    public static void updateEntity(Division division,
                                    DivisionRequestDto dto,
                                    Country country) {

        division.setName(dto.getName());
        division.setNameBN(dto.getNameBN());

        division.setCountry(country);
    }
}
