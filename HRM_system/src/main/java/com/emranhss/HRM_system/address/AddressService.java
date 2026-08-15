package com.emranhss.HRM_system.address;

import java.util.List;

public interface AddressService {

    Address create(AddressRequestDto dto);


    AddressResponseDto getById(Long id);


    List<AddressResponseDto> getAll();


    Address update(Long id, AddressRequestDto dto);


    void delete(Long id);


}
