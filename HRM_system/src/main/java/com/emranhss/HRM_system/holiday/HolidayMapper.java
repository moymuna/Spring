package com.emranhss.HRM_system.holiday;

public class HolidayMapper {
    
    public static Holiday toEntity(HolidayRequestDto dto) {

        Holiday holiday = new Holiday();

        holiday.setName(dto.getName());
        holiday.setDate(dto.getDate());
        holiday.setRecurringYearly(dto.getRecurringYearly());
        holiday.setDescription(dto.getDescription());

        return holiday;
    }

    
    public static HolidayResponseDto toResponse(Holiday holiday) {

        HolidayResponseDto dto = new HolidayResponseDto();

        dto.setId(holiday.getId());
        dto.setName(holiday.getName());
        dto.setDate(holiday.getDate());
        dto.setRecurringYearly(holiday.isRecurringYearly());
        dto.setDescription(holiday.getDescription());

        return dto;
    }

    
    public static void updateEntity(Holiday holiday,
                                    HolidayRequestDto dto) {

        holiday.setName(dto.getName());
        holiday.setDate(dto.getDate());
        holiday.setRecurringYearly(dto.getRecurringYearly());
        holiday.setDescription(dto.getDescription());
    }
}
