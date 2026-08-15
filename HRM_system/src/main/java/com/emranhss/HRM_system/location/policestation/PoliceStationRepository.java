package com.emranhss.HRM_system.location.policestation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoliceStationRepository extends JpaRepository<PoliceStation,Long> {

    List<PoliceStation> findByDistrictId(Long districtId);

    List<PoliceStation> findByDistrictDistrictsName(String districtName);

}

