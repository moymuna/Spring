package com.emranhss.HRM_system.office;

import com.emranhss.HRM_system.address.Address;
import com.emranhss.HRM_system.address.AddressRepository;
import com.emranhss.HRM_system.address.AddressResponseDto;
import com.emranhss.HRM_system.address.AddressService;
import com.emranhss.HRM_system.location.policestation.PoliceStation;
import com.emranhss.HRM_system.location.policestation.PoliceStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {


    private final OfficeRepository officeRepository;


    private final AddressService addressService;
    private final OfficeMapper officeMapper;



    @Override
    @Transactional
    public OfficeResponseDto create(OfficeRequestDto dto) {

        Office office = new Office();

        office.setOfficeName(dto.getOfficeName());
        office.setOfficeCode(dto.getOfficeCode());
        office.setPhone(dto.getPhone());
        office.setEmail(dto.getEmail());

        Address address = addressService.create(dto.getAddress());

        office.setAddress(address);

        Office saved = officeRepository.save(office);

        return officeMapper.toResponse(saved);
    }





    @Override
    @Transactional(readOnly = true)
    public OfficeResponseDto getById(Long id){

        return officeRepository.findById(id)
                .map(officeMapper::toResponse)
                .orElseThrow();

    }




    @Override
    @Transactional(readOnly = true)
    public List<OfficeResponseDto> getAll(){


        return officeRepository.findAll()
                .stream()
                .map(officeMapper::toResponse)
                .collect(Collectors.toList());

    }





    @Override
    @Transactional
    public OfficeResponseDto update(Long id, OfficeRequestDto dto){


        Office office =
                officeRepository.findById(id)
                        .orElseThrow();



        office.setOfficeName(dto.getOfficeName());

        office.setOfficeCode(dto.getOfficeCode());

        office.setPhone(dto.getPhone());

        office.setEmail(dto.getEmail());


        Address updatedAddress =
                addressService.update(office.getAddress().getId(),dto.getAddress());

        office.setAddress(updatedAddress);

        officeRepository.save(office);


        return officeMapper.toResponse(
                officeRepository.save(office)
        );


    }




    @Override
    @Transactional
    public void delete(Long id){

        officeRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<OfficeResponseDto> search(String keyword) {
        return officeRepository.searchOffices(keyword)
                .stream()
                .map(officeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getOfficeCount() {
        return officeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfficeResponseDto> getOffices(Pageable pageable) {
        return officeRepository.findAll(pageable)
                .map(officeMapper::toResponse);
    }
}
