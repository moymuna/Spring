package com.emranhss.HRM_system.holiday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday,Long> {
    Optional<Holiday> findByDate(LocalDate date);

    boolean existsByDate(LocalDate date);

    Optional<Holiday> findByName(String name);

    boolean existsByName(String name);

    List<Holiday> findByRecurringYearlyTrue();

    List<Holiday> findByDateGreaterThanEqualOrderByDateAsc(LocalDate from);
}
