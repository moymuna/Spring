package com.emranhss.HRM_system.location.district;

import lombok.Data;

@Data
public class DistrictResponseDto {
    private Long id;

    private String districtsName;

    private String nameBN;

    private String districtCode;

    private Long divisionId;

    private String divisionName;
}
