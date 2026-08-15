package com.emranhss.HRM_system.attendance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.AttendanceStatus;
import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.holiday.Holiday;
import com.emranhss.HRM_system.holiday.HolidayRepository;
import com.emranhss.HRM_system.leave.leave.LeaveRepository;
import com.emranhss.HRM_system.leave.leavebalance.LeaveBalance;
import com.emranhss.HRM_system.leave.leavebalance.LeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceAutoStatusJob {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final HolidayRepository holidayRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void markMissedAttendanceForYesterday() {
        LocalDate date = LocalDate.now().minusDays(1);
        run(date);
    }

    public void run(LocalDate date) {

        List<Employee> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);

        AttendanceStatus dayStatus = resolveNonWorkingStatus(date);

        for (Employee employee : activeEmployees) {

            boolean alreadyRecorded = attendanceRepository
                    .findByEmployeeIdAndDate(employee.getId(), date)
                    .isPresent();

            if (alreadyRecorded) {
                continue;
            }

            AttendanceStatus status = dayStatus;

            if (status == null) {
                status = leaveRepository.findApprovedLeaveCoveringDate(employee.getId(), date).isPresent()
                        ? AttendanceStatus.ON_LEAVE
                        : AttendanceStatus.ABSENT;
            }

            Attendance attendance = new Attendance();
            attendance.setEmployee(employee);
            attendance.setDate(date);
            attendance.setStatus(status);

            attendanceRepository.save(attendance);

            if (status == AttendanceStatus.ABSENT) {
                deductLeaveBalanceForAbsence(employee, date);
            }
        }

        log.info("Auto-attendance job processed {} active employees for {}", activeEmployees.size(), date);
    }

    
    private AttendanceStatus resolveNonWorkingStatus(LocalDate date) {

        boolean isHoliday = holidayRepository.findByDate(date).isPresent()
                || holidayRepository.findByRecurringYearlyTrue().stream()
                        .anyMatch(h -> matchesMonthAndDay(h, date));

        if (isHoliday) {
            return AttendanceStatus.HOLIDAY;
        }

        
        if (date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return AttendanceStatus.WEEK_OFF;
        }

        return null;
    }

    private boolean matchesMonthAndDay(Holiday holiday, LocalDate date) {
        return holiday.getDate().getMonthValue() == date.getMonthValue()
                && holiday.getDate().getDayOfMonth() == date.getDayOfMonth();
    }

    
    private void deductLeaveBalanceForAbsence(Employee employee, LocalDate date) {

        List<LeaveBalance> balances = leaveBalanceRepository
                .findByEmployeeAndYear(employee, date.getYear());

        balances.stream()
                .filter(b -> b.getRemaining() > 0)
                .min(Comparator.comparingLong(LeaveBalance::getId))
                .ifPresentOrElse(
                        balance -> {
                            balance.setUsed(balance.getUsed() + 1);
                            leaveBalanceRepository.save(balance);
                        },
                        () -> log.info("No leave balance available to deduct for employee {} on {}",
                                employee.getId(), date)
                );
    }
}
