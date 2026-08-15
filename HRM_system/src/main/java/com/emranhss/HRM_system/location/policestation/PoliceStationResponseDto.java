package com.emranhss.HRM_system.location.policestation;

import lombok.Data;

@Data
public class PoliceStationResponseDto {
    private Long id;

    private String name;

    private String nameBn;

    private String postalCode;

    private Long districtId;

    private String districtName;

    private Long divisionId;

    private String divisionName;

    private Long countryId;

    private String countryName;
}

