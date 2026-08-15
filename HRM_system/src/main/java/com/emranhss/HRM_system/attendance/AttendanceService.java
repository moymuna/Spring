package com.emranhss.HRM_system.attendance;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {

    AttendanceResponseDto saveAttendance(AttendanceRequestDto dto);

    AttendanceResponseDto getAttendanceById(Long id);

    List<AttendanceResponseDto> getAllAttendance();

    AttendanceResponseDto updateAttendance(Long id, AttendanceRequestDto dto);

    void deleteAttendance(Long id);

    AttendanceResponseDto clockIn(Long employeeId);

    AttendanceResponseDto clockOut(Long employeeId);

    AttendanceResponseDto getTodayAttendance(Long employeeId);
    List<AttendanceResponseDto> getAttendanceByMonth(
            Long employeeId,
            int year,
            int month
    );

    List<AttendanceResponseDto> searchAttendance(String keyword);

    long getAttendanceCount();

    Page<AttendanceResponseDto> getAttendance(Pageable pageable);

    AttendanceMonthlySummaryDto getMonthlySummary(Long employeeId, int year, int month);

    long getTodayAttendanceCount();

}
