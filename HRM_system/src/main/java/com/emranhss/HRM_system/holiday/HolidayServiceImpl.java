package com.emranhss.HRM_system.holiday;

import com.emranhss.HRM_system.exception.ConflictException;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {
    
    private final HolidayRepository holidayRepository;

    
    @Override
    public HolidayResponseDto saveHoliday(HolidayRequestDto dto) {

        
        if (holidayRepository.existsByName(dto.getName())) {
            throw new ConflictException("Holiday name already exists.");
        }

        
        if (holidayRepository.existsByDate(dto.getDate())) {
            throw new ConflictException("Holiday date already exists.");
        }

        
        Holiday holiday = HolidayMapper.toEntity(dto);

        
        holiday = holidayRepository.save(holiday);

        
        return HolidayMapper.toResponse(holiday);
    }

    
    @Override
    public HolidayResponseDto getHolidayById(Long id) {

        
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found."));

        
        return HolidayMapper.toResponse(holiday);
    }

    
    @Override
    public List<HolidayResponseDto> getAllHolidays() {

        
        return holidayRepository.findAll()
                .stream()
                .map(HolidayMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public HolidayResponseDto updateHoliday(Long id, HolidayRequestDto dto) {

        
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found."));

        
        holidayRepository.findByName(dto.getName())
                .ifPresent(existingHoliday -> {
                    if (!existingHoliday.getId().equals(id)) {
                        throw new ConflictException("Holiday name already exists.");
                    }
                });

        
        holidayRepository.findByDate(dto.getDate())
                .ifPresent(existingHoliday -> {
                    if (!existingHoliday.getId().equals(id)) {
                        throw new ConflictException("Holiday date already exists.");
                    }
                });

        
        HolidayMapper.updateEntity(holiday, dto);

        
        holiday = holidayRepository.save(holiday);

        
        return HolidayMapper.toResponse(holiday);
    }

    
    @Override
    public void deleteHoliday(Long id) {

        
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found."));

        
        holidayRepository.delete(holiday);
    }

    
    @Override
    public HolidayResponseDto getHolidayByName(String name) {

        
        Holiday holiday = holidayRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found."));

        
        return HolidayMapper.toResponse(holiday);
    }

    
    @Override
    public HolidayResponseDto getHolidayByDate(LocalDate date) {

        
        Holiday holiday = holidayRepository.findByDate(date)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found."));


        return HolidayMapper.toResponse(holiday);
    }

    /**
     * Holidays still ahead of today, soonest first. A holiday flagged as recurring
     * is reported on its next anniversary rather than the year it was entered in.
     */
    @Override
    public List<HolidayResponseDto> getUpcomingHolidays(int limit) {

        LocalDate today = LocalDate.now();

        return holidayRepository.findAll()
                .stream()
                .map(holiday -> {
                    HolidayResponseDto dto = HolidayMapper.toResponse(holiday);
                    dto.setDate(nextOccurrence(holiday, today));
                    return dto;
                })
                .filter(dto -> dto.getDate() != null && !dto.getDate().isBefore(today))
                .sorted(java.util.Comparator.comparing(HolidayResponseDto::getDate))
                .limit(Math.max(limit, 1))
                .collect(Collectors.toList());
    }

    private LocalDate nextOccurrence(Holiday holiday, LocalDate today) {

        LocalDate date = holiday.getDate();

        if (date == null || !holiday.isRecurringYearly()) {
            return date;
        }

        LocalDate thisYear = withYearSafe(date, today.getYear());

        return thisYear.isBefore(today) ? withYearSafe(date, today.getYear() + 1) : thisYear;
    }

    /** Feb 29 has no counterpart in a common year, so fall back to Feb 28. */
    private LocalDate withYearSafe(LocalDate date, int year) {

        int day = Math.min(date.getDayOfMonth(), date.getMonth().length(java.time.Year.isLeap(year)));

        return LocalDate.of(year, date.getMonth(), day);
    }

}
