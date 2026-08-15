package com.emranhss.HRM_system.location.district;

import com.emranhss.HRM_system.location.division.Division;

public class DistrictMapper {
    
    public static District toEntity(DistrictRequestDto dto,
                                    Division division) {

        District district = new District();

        
        district.setDistrictsName(dto.getDistrictsName());
        district.setNameBN(dto.getNameBN());
        district.setDistrictCode(dto.getDistrictCode());

        
        district.setDivision(division);

        return district;
    }

    
    public static DistrictResponseDto toResponse(District district) {

        DistrictResponseDto dto = new DistrictResponseDto();

        dto.setId(district.getId());
        dto.setDistrictsName(district.getDistrictsName());
        dto.setNameBN(district.getNameBN());
        dto.setDistrictCode(district.getDistrictCode());

        if (district.getDivision() != null) {

            dto.setDivisionId(district.getDivision().getId());
            dto.setDivisionName(district.getDivision().getName());
        }

        return dto;
    }

    
    public static void updateEntity(District district,
                                    DistrictRequestDto dto,
                                    Division division) {

        district.setDistrictsName(dto.getDistrictsName());
        district.setNameBN(dto.getNameBN());
        district.setDistrictCode(dto.getDistrictCode());

        district.setDivision(division);
    }
}
