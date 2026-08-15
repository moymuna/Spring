package com.emranhss.HRM_system.location.division;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DivisionRepository extends JpaRepository<Division,Long> {


    Optional<Division> findByName(String name);

    boolean existsByName(String name);

    List<Division> findByCountryId(Long countryId);

    List<Division> findByCountryCountryName(String countryName);

}
