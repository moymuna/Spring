package com.emranhss.HRM_system.holiday;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HolidayService {

    
    HolidayResponseDto saveHoliday(HolidayRequestDto dto);

    
    HolidayResponseDto getHolidayById(Long id);

    
    List<HolidayResponseDto> getAllHolidays();

    
    HolidayResponseDto updateHoliday(Long id, HolidayRequestDto dto);

    
    void deleteHoliday(Long id);

    
    HolidayResponseDto getHolidayByName(String name);


    HolidayResponseDto getHolidayByDate(java.time.LocalDate date);


    List<HolidayResponseDto> getUpcomingHolidays(int limit);
}
