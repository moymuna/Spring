package com.emranhss.HRM_system.location.policestation;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.location.district.District;
import com.emranhss.HRM_system.location.district.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoliceStationServiceImpl implements PoliceStationService {
    @Autowired
    private PoliceStationRepository stationRepository;

    @Autowired
    private DistrictRepository districtRepository;



    @Override
    @Transactional
    public PoliceStation save(PoliceStationRequestDto dto) {


        District district =
                districtRepository.findById(dto.getDistrictId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("District not found"
                                )
                        );


        PoliceStation ps = new PoliceStation();


        ps.setName(dto.getName());

        ps.setNameBn(dto.getNameBn());

        ps.setPostalCode(dto.getPostalCode());


        ps.setDistrict(district);



        return stationRepository.save(ps);

    }

    @Override
    @Transactional(readOnly = true)
    public List<PoliceStation> findAll() {
        return stationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PoliceStation> getById(Long id) {
        return stationRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        stationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoliceStationResponseDto> findByDistrictId(Long districtId) {
        List<PoliceStation> list= stationRepository.findByDistrictId(districtId);
        return list.stream().map(PoliceStationMapper::toDTO).collect(Collectors.toList());
    }



    @Override
    @Transactional(readOnly = true)
    public List<PoliceStationResponseDto> findByDistrictName(String districtName) {
        List<PoliceStation> list= stationRepository.findByDistrictDistrictsName(districtName);
        return list.stream().map(PoliceStationMapper::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public PoliceStation update(Long id, PoliceStationRequestDto dto) {


        PoliceStation policeStation =
                stationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Police Station not found with id: " + id
                                )
                        );


        

        if(dto.getName() != null) {
            policeStation.setName(dto.getName());
        }


        if(dto.getNameBn() != null) {
            policeStation.setNameBn(dto.getNameBn());
        }


        if(dto.getPostalCode() != null) {
            policeStation.setPostalCode(dto.getPostalCode());
        }



        

        if(dto.getDistrictId() != null) {


            District district =
                    districtRepository.findById(dto.getDistrictId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("District not found with id: "
                                                    + dto.getDistrictId()
                                    )
                            );


            policeStation.setDistrict(district);
        }



        return stationRepository.save(policeStation);

    }

}
