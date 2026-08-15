package com.emranhss.HRM_system.address;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.location.country.Country;
import com.emranhss.HRM_system.location.country.CountryRepository;
import com.emranhss.HRM_system.location.district.District;
import com.emranhss.HRM_system.location.district.DistrictRepository;
import com.emranhss.HRM_system.location.division.Division;
import com.emranhss.HRM_system.location.division.DivisionRepository;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import com.emranhss.HRM_system.location.policestation.PoliceStationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {


    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    private final PoliceStationRepository policeStationRepository;



    @Override
    public Address create(AddressRequestDto dto) {

        Address address = new Address();

        mapToEntity(address, dto);

        return addressRepository.save(address);
    }



    private void mapToEntity(Address address, AddressRequestDto dto){


        address.setAddressLine1(dto.getAddressLine1());

        address.setAddressLine2(dto.getAddressLine2());

        address.setPostOffice(dto.getPostOffice());

        address.setPostalCode(dto.getPostalCode());

        if(dto.getPoliceStationId()!=null){

            PoliceStation ps =
                    policeStationRepository.findById(dto.getPoliceStationId())
                            .orElseThrow();

            address.setPoliceStation(ps);

        }


    }





    @Override
    public AddressResponseDto getById(Long id){

        Address address =
                addressRepository.findById(id)
                        .orElseThrow();


        return addressMapper.toResponse(address);

    }




    @Override
    public List<AddressResponseDto> getAll(){

        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());

    }
























    @Override
    public Address update(Long id, AddressRequestDto dto) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setPostOffice(dto.getPostOffice());
        address.setPostalCode(dto.getPostalCode());

        address.setPoliceStation(
                policeStationRepository.findById(dto.getPoliceStationId())
                        .orElseThrow()
        );

        return addressRepository.save(address);
    }





    @Override
    public void delete(Long id){

        addressRepository.deleteById(id);

    }


}