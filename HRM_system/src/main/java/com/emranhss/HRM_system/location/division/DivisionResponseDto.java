package com.emranhss.HRM_system.location.division;

import lombok.Data;

@Data
public class DivisionResponseDto {
    private Long id;

    private String name;

    private String nameBN;

    private Long countryId;

    private String countryName;
}
