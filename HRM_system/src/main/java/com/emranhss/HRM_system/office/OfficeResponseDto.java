package com.emranhss.HRM_system.office;

import com.emranhss.HRM_system.address.AddressResponseDto;
import lombok.Data;

@Data
public class OfficeResponseDto {

    private Long id;

    private String officeName;

    private String officeCode;

    private String phone;

    private String email;


    private AddressResponseDto address;

}
