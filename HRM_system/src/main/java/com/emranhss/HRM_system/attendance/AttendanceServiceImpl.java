package com.emranhss.HRM_system.attendance;

import com.emranhss.HRM_system.exception.ConflictException;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public AttendanceResponseDto saveAttendance(AttendanceRequestDto dto) {

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Attendance attendance = AttendanceMapper.toEntity(dto, employee);

        attendance = attendanceRepository.save(attendance);

        return AttendanceMapper.toResponse(attendance);
    }

    @Override
    public AttendanceResponseDto getAttendanceById(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        return AttendanceMapper.toResponse(attendance);
    }

    @Override
    public List<AttendanceResponseDto> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(AttendanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceResponseDto updateAttendance(Long id, AttendanceRequestDto dto) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        AttendanceMapper.updateEntity(attendance, dto, employee);

        attendance = attendanceRepository.save(attendance);

        return AttendanceMapper.toResponse(attendance);
    }

    @Override
    public void deleteAttendance(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        attendanceRepository.delete(attendance);
    }

    @Override
    public AttendanceResponseDto clockIn(Long employeeId) {

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, today)
                .orElse(null);

        if (attendance != null) {
            throw new ConflictException("Already checked in today.");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        attendance = new Attendance();

        attendance.setDate(today);

        attendance.setEmployee(employee);

        attendance.setStatus(AttendanceStatus.PRESENT);

        attendance.setCheckInTime(LocalDateTime.now());

        attendance = attendanceRepository.save(attendance);

        return AttendanceMapper.toResponse(attendance);
    }

    @Override
    public AttendanceResponseDto clockOut(Long employeeId) {

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attendance not found."));

        if (attendance.getCheckOutTime() != null) {
            throw new ConflictException("Already checked out.");
        }

        attendance.setCheckOutTime(LocalDateTime.now());

        attendance = attendanceRepository.save(attendance);

        return AttendanceMapper.toResponse(attendance);
    }


    @Override
    public AttendanceResponseDto getTodayAttendance(Long employeeId) {

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No attendance found"));

        return AttendanceMapper.toResponse(attendance);
    }

    @Override
    public List<AttendanceResponseDto> getAttendanceByMonth(
            Long employeeId,
            int year,
            int month) {

        LocalDate start = LocalDate.of(year, month, 1);

        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return attendanceRepository
                .findByEmployeeIdAndDateBetween(employeeId, start, end)
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponseDto> searchAttendance(String keyword) {
        return attendanceRepository.searchAttendance(keyword)
                .stream()
                .map(AttendanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long getAttendanceCount() {
        return attendanceRepository.count();
    }

    @Override
    public Page<AttendanceResponseDto> getAttendance(Pageable pageable) {
        return attendanceRepository.findAll(pageable)
                .map(AttendanceMapper::toResponse);
    }

    @Override
    public AttendanceMonthlySummaryDto getMonthlySummary(Long employeeId, int year, int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Attendance> records = attendanceRepository
                .findByEmployeeIdAndDateBetween(employeeId, start, end);

        AttendanceMonthlySummaryDto summary = new AttendanceMonthlySummaryDto();
        summary.setEmployeeId(employeeId);
        summary.setYear(year);
        summary.setMonth(month);

        double totalHours = 0.0;
        for (Attendance a : records) {
            switch (a.getStatus()) {
                case PRESENT -> summary.setPresentDays(summary.getPresentDays() + 1);
                case ABSENT -> summary.setAbsentDays(summary.getAbsentDays() + 1);
                case HALF_DAY -> summary.setHalfDays(summary.getHalfDays() + 1);
                case ON_LEAVE -> summary.setOnLeaveDays(summary.getOnLeaveDays() + 1);
                case HOLIDAY -> summary.setHolidayDays(summary.getHolidayDays() + 1);
                case WEEK_OFF -> summary.setWeekOffDays(summary.getWeekOffDays() + 1);
                case WORK_FROM_HOME -> summary.setWorkFromHomeDays(summary.getWorkFromHomeDays() + 1);
            }
            Double hours = a.getWorkedHours();
            if (hours != null) {
                totalHours += hours;
            }
        }
        summary.setTotalWorkedHours(totalHours);

        return summary;
    }

    @Override
    public long getTodayAttendanceCount() {
        return attendanceRepository.countByDate(LocalDate.now());
    }

}
