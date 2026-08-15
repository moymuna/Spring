package com.emranhss.HRM_system.office;

import com.emranhss.HRM_system.address.AddressRequestDto;
import lombok.Data;

@Data
public class OfficeRequestDto {

    private String officeName;

    private String officeCode;

    private String phone;

    private String email;


    private AddressRequestDto address;


}
