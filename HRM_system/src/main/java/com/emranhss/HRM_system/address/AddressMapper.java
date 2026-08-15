package com.emranhss.HRM_system.address;


import org.springframework.stereotype.Component;

@Component
public class AddressMapper {


    public AddressResponseDto toResponse(Address address){


        AddressResponseDto dto = new AddressResponseDto();

        dto.setId(address.getId());
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setPostOffice(address.getPostOffice());
        dto.setPostalCode(address.getPostalCode());
        if(address.getPoliceStation() != null){
            dto.setPoliceStationName(address.getPoliceStation().getName());
            dto.setPoliceStationId(address.getPoliceStation().getId());

            dto.setDistrictName(address.getPoliceStation().getDistrict().getDistrictsName());
            dto.setDistrictId(address.getPoliceStation().getId());

            dto.setDivisionName(address.getPoliceStation().getDistrict().getDivision().getName());
            dto.setDivisionId(address.getPoliceStation().getDistrict().getDivision().getId());

            dto.setCountryName(address.getPoliceStation().getDistrict().getDivision().getCountry().getCountryName());
            dto.setCountryId(address.getPoliceStation().getDistrict().getDivision().getCountry().getId());

        }
        return dto;

    }
}
