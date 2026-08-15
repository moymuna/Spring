package com.emranhss.HRM_system.location.country;

import lombok.Data;

@Data
public class CountryResponseDto {
    private Long id;

    private String countryName;

    private String code;

    private String phoneCode;
}
