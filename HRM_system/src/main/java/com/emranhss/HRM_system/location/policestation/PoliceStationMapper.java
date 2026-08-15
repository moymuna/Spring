package com.emranhss.HRM_system.location.policestation;

import com.emranhss.HRM_system.location.district.District;
import com.emranhss.HRM_system.location.division.Division;

public class PoliceStationMapper {  
    public static PoliceStationResponseDto toDTO(PoliceStation ps) {

        PoliceStationResponseDto dto = new PoliceStationResponseDto();

        dto.setId(ps.getId());
        dto.setName(ps.getName());
        dto.setNameBn(ps.getNameBn());
        dto.setPostalCode(ps.getPostalCode());


        
        District district = ps.getDistrict();
        if (district != null) {
            dto.setDistrictId(district.getId());
            dto.setDistrictName(district.getDistrictsName());

            
            Division division = district.getDivision();
            if (division != null) {
                dto.setDivisionId(division.getId());
                dto.setDivisionName(division.getName());

                
                if (division.getCountry() != null) {
                    dto.setCountryId(division.getCountry().getId());
                    dto.setCountryName(division.getCountry().getCountryName());
                }
            }
        }

        return dto;
    }

    
    public static PoliceStation toEntity(PoliceStationRequestDto dto) {

        PoliceStation ps = new PoliceStation();

        ps.setName(dto.getName());
        ps.setNameBn(dto.getNameBn());
        ps.setPostalCode(dto.getPostalCode());

        

        return ps;
    }

    
    public static void updateEntity(PoliceStation ps, PoliceStationRequestDto dto) {

        if (dto.getName() != null)       ps.setName(dto.getName());
        if (dto.getNameBn() != null)     ps.setNameBn(dto.getNameBn());
        if (dto.getPostalCode() != null) ps.setPostalCode(dto.getPostalCode());

        
    }
}
