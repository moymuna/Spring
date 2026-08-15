package com.emranhss.HRM_system.dashboard;

import com.emranhss.HRM_system.advance.AdvanceRepository;
import com.emranhss.HRM_system.attendance.Attendance;
import com.emranhss.HRM_system.attendance.AttendanceRepository;
import com.emranhss.HRM_system.department.DepartmentRepository;
import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.AdvanceStatus;
import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.enums.LeaveStatus;
import com.emranhss.HRM_system.leave.leave.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final AdvanceRepository advanceRepository;

    public AdminDashboardDto getAdminDashboard(int year) {

        AdminDashboardDto dto = new AdminDashboardDto();

        LocalDate today = LocalDate.now();
        List<Employee> employees = employeeRepository.findAll();

        dto.setTotalEmployees(employees.size());
        dto.setActiveEmployees(employeeRepository.countByStatus(EmployeeStatus.ACTIVE));
        dto.setInactiveEmployees(dto.getTotalEmployees() - dto.getActiveEmployees());
        dto.setDepartments(departmentRepository.count());

        YearMonth thisMonth = YearMonth.from(today);
        dto.setNewHiresThisMonth(employees.stream()
                .filter(e -> inMonth(e.getJoiningDate(), thisMonth))
                .count());

        dto.setOnLeaveToday(employees.stream()
                .filter(e -> leaveRepository.findApprovedLeaveCoveringDate(e.getId(), today).isPresent())
                .count());

        dto.setPendingLeaveApprovals(leaveRepository.findByStatus(LeaveStatus.PENDING).size());
        dto.setPendingAdvanceApprovals(advanceRepository.countByStatus(AdvanceStatus.PENDING));

        for (Object[] row : employeeRepository.countActiveEmployeesByDepartment()) {
            String name = row[0] == null ? "Unassigned" : row[0].toString();
            dto.getHeadcountByDepartment().put(name, ((Number) row[1]).longValue());
        }

        dto.setAttendanceToday(attendanceRepository.findByDate(today).stream()
                .collect(Collectors.groupingBy(
                        (Attendance a) -> a.getStatus() == null ? "UNKNOWN" : a.getStatus().name(),
                        LinkedHashMap::new,
                        Collectors.counting())));

        dto.setHeadcountTrend(buildHeadcountTrend(employees, year));

        return dto;
    }

    /**
     * Headcount at the end of each month: everyone who had joined by then and had
     * not yet left. Gives the dashboard a real staffing curve rather than a flat line.
     */
    private Map<Integer, Long> buildHeadcountTrend(List<Employee> employees, int year) {

        Map<Integer, Long> trend = new LinkedHashMap<>();

        for (int month = 1; month <= 12; month++) {

            LocalDate monthEnd = YearMonth.of(year, month).atEndOfMonth();

            long count = employees.stream()
                    .filter(e -> {
                        LocalDate joined = toLocalDate(e.getJoiningDate());
                        if (joined == null || joined.isAfter(monthEnd)) {
                            return false;
                        }
                        LocalDate exited = toLocalDate(e.getDateOfExit());
                        return exited == null || exited.isAfter(monthEnd);
                    })
                    .count();

            trend.put(month, count);
        }

        return trend;
    }

    public List<BirthdayDto> getUpcomingBirthdays(int withinDays) {

        LocalDate today = LocalDate.now();
        List<BirthdayDto> result = new ArrayList<>();

        for (Employee employee : employeeRepository.findAll()) {

            LocalDate dob = toLocalDate(employee.getDateOfBirth());
            if (dob == null || employee.getStatus() != EmployeeStatus.ACTIVE) {
                continue;
            }

            LocalDate next = nextOccurrence(dob, today);
            long daysUntil = ChronoUnit.DAYS.between(today, next);

            if (daysUntil > withinDays) {
                continue;
            }

            BirthdayDto dto = new BirthdayDto();
            dto.setEmployeeId(employee.getId());
            dto.setEmployeeCode(employee.getEmployeeCode());
            dto.setImage(employee.getImage());
            dto.setNextBirthday(next);
            dto.setDaysUntil((int) daysUntil);
            if (employee.getUser() != null) {
                dto.setEmployeeName(employee.getUser().getFullName());
            }
            result.add(dto);
        }

        result.sort(Comparator.comparingInt(BirthdayDto::getDaysUntil));
        return result;
    }

    /** Feb 29 falls back to Feb 28 in a common year. */
    private LocalDate nextOccurrence(LocalDate dob, LocalDate today) {

        LocalDate thisYear = withYearSafe(dob, today.getYear());
        return thisYear.isBefore(today) ? withYearSafe(dob, today.getYear() + 1) : thisYear;
    }

    private LocalDate withYearSafe(LocalDate date, int year) {
        int day = Math.min(date.getDayOfMonth(), date.getMonth().length(java.time.Year.isLeap(year)));
        return LocalDate.of(year, date.getMonth(), day);
    }

    private boolean inMonth(Date date, YearMonth month) {
        LocalDate local = toLocalDate(date);
        return local != null && YearMonth.from(local).equals(month);
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        if (date instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
