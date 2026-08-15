package com.emranhss.HRM_system.address;

import lombok.Data;


@Data
public class AddressResponseDto {


    private Long id;

    private String addressLine1;

    private String addressLine2;

    private String postOffice;

    private String postalCode;

    private Long countryId;
    private String countryName;

    private Long divisionId;
    private String divisionName;

    private Long districtId;
    private String districtName;

    private Long policeStationId;
    private String policeStationName;

}
