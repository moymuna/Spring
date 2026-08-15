package com.emranhss.HRM_system.address;

import lombok.Data;

@Data
public class AddressRequestDto {


    private String addressLine1;

    private String addressLine2;

    private String postOffice;

    private String postalCode;

    private Long policeStationId;


}