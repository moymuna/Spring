package com.emranhss.HRM_system.leave.leavebalance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.enums.EmployeeStatus;
import com.emranhss.HRM_system.leave.leavetype.LeaveType;
import com.emranhss.HRM_system.leave.leavetype.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class LeaveBalanceAccrualJob {

    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    
    @Scheduled(cron = "0 0 2 1 1 *")
    @Transactional
    public void accrueForNewYear() {
        run(LocalDate.now().getYear());
    }

    
    public int run(int year) {

        List<Employee> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        int created = 0;

        for (Employee employee : activeEmployees) {
            for (LeaveType leaveType : leaveTypes) {

                // Unpaid leave is not balance-tracked; its cost is the payroll deduction.
                if (!leaveType.isPaid()) {
                    continue;
                }

                boolean exists = leaveBalanceRepository
                        .findByEmployeeAndLeaveTypeAndYear(employee, leaveType, year)
                        .isPresent();

                if (exists) {
                    continue;
                }

                double entitled = leaveType.getMaxDaysPerYear();

                // Unused days from last year roll over, capped per leave type.
                int carryCap = leaveType.getMaxCarryForwardDays() == null
                        ? 0
                        : leaveType.getMaxCarryForwardDays();

                if (carryCap > 0) {
                    double previousRemaining = leaveBalanceRepository
                            .findByEmployeeAndLeaveTypeAndYear(employee, leaveType, year - 1)
                            .map(LeaveBalance::getRemaining)
                            .orElse(0.0);

                    entitled += Math.min(Math.max(previousRemaining, 0.0), carryCap);
                }

                LeaveBalance balance = new LeaveBalance();
                balance.setEmployee(employee);
                balance.setLeaveType(leaveType);
                balance.setYear(year);
                balance.setTotalEntitled(entitled);
                balance.setUsed(0.0);

                leaveBalanceRepository.save(balance);
                created++;
            }
        }

        log.info("Leave balance accrual for {}: created {} new balance record(s) for {} active employee(s).",
                year, created, activeEmployees.size());

        return created;
    }
}
