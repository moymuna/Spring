package com.emranhss.HRM_system.location.policestation;

import com.emranhss.HRM_system.location.district.District;
import com.emranhss.HRM_system.location.district.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public interface PoliceStationService {
    PoliceStation save(PoliceStationRequestDto dto);
    List<PoliceStation> findAll();
    Optional<PoliceStation> getById(Long id);
    void delete(Long id);

    List<PoliceStationResponseDto> findByDistrictId(Long  districtId);

    List<PoliceStationResponseDto> findByDistrictName(String districtName);
    PoliceStation update(Long id, PoliceStationRequestDto dto);

}
