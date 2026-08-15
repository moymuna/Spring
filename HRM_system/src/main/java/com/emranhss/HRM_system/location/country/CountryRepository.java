package com.emranhss.HRM_system.location.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {
    Optional<Country> findByCountryName(String countryName);

    Optional<Country> findByCode(String code);

    boolean existsByCountryName(String countryName);

    boolean existsByCode(String code);

}
