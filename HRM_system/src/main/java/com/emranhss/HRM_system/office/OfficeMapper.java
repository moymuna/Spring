package com.emranhss.HRM_system.office;

import com.emranhss.HRM_system.address.Address;
import com.emranhss.HRM_system.address.AddressMapper;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfficeMapper {

    private final AddressMapper addressMapper;

    public OfficeResponseDto toResponse(Office office){


        OfficeResponseDto dto =
                new OfficeResponseDto();


        dto.setId(office.getId());

        dto.setOfficeName(office.getOfficeName());

        dto.setOfficeCode(office.getOfficeCode());

        dto.setPhone(office.getPhone());

        dto.setEmail(office.getEmail());


        if(office.getAddress()!=null)
        {
            dto.setAddress(
                    addressMapper.toResponse(
                            office.getAddress()
                    )
            );
        }


        return dto;

    }

}
