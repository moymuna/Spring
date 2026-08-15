package com.emranhss.HRM_system.leave.leavebalance;

import com.emranhss.HRM_system.exception.ConflictException;
import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.leave.leavetype.LeaveType;
import com.emranhss.HRM_system.leave.leavetype.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    
    @Override
    public LeaveBalanceResponseDto saveLeaveBalance(LeaveBalanceRequestDto dto) {

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        
        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave type not found."));

        
        leaveBalanceRepository
                .findByEmployeeAndLeaveTypeAndYear(
                        employee,
                        leaveType,
                        dto.getYear())
                .ifPresent(lb -> {
                    throw new ConflictException(
                            "Leave balance already exists for this employee, leave type and year.");
                });

        
        LeaveBalance leaveBalance =
                LeaveBalanceMapper.toEntity(dto, employee, leaveType);

        
        leaveBalance = leaveBalanceRepository.save(leaveBalance);

        
        return LeaveBalanceMapper.toResponse(leaveBalance);
    }

    
    @Override
    public LeaveBalanceResponseDto getLeaveBalanceById(Long id) {

        LeaveBalance leaveBalance = leaveBalanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave balance not found."));

        return LeaveBalanceMapper.toResponse(leaveBalance);
    }

    
    @Override
    public List<LeaveBalanceResponseDto> getAllLeaveBalances() {

        return leaveBalanceRepository.findAll()
                .stream()
                .map(LeaveBalanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public LeaveBalanceResponseDto updateLeaveBalance(Long id,
                                                      LeaveBalanceRequestDto dto) {

        
        LeaveBalance leaveBalance = leaveBalanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave balance not found."));

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        
        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave type not found."));

        
        leaveBalanceRepository
                .findByEmployeeAndLeaveTypeAndYear(
                        employee,
                        leaveType,
                        dto.getYear())
                .ifPresent(existing -> {

                    if (existing.getId() != id) {

                        throw new ConflictException(
                                "Leave balance already exists.");
                    }
                });

        
        LeaveBalanceMapper.updateEntity(
                leaveBalance,
                dto,
                employee,
                leaveType);

        
        leaveBalance = leaveBalanceRepository.save(leaveBalance);

        return LeaveBalanceMapper.toResponse(leaveBalance);
    }

    
    @Override
    public void deleteLeaveBalance(Long id) {

        LeaveBalance leaveBalance = leaveBalanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave balance not found."));

        leaveBalanceRepository.delete(leaveBalance);
    }

    
    @Override
    public List<LeaveBalanceResponseDto> getLeaveBalancesByEmployee(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        return leaveBalanceRepository.findByEmployee(employee)
                .stream()
                .map(LeaveBalanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public List<LeaveBalanceResponseDto> getLeaveBalancesByLeaveType(Long leaveTypeId) {

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave type not found."));

        return leaveBalanceRepository.findByLeaveType(leaveType)
                .stream()
                .map(LeaveBalanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public List<LeaveBalanceResponseDto> getLeaveBalancesByYear(Integer year) {

        return leaveBalanceRepository.findByYear(year)
                .stream()
                .map(LeaveBalanceMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public LeaveBalanceResponseDto getEmployeeLeaveBalance(
            Long employeeId,
            Long leaveTypeId,
            Integer year) {

        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave type not found."));

        
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByEmployeeAndLeaveTypeAndYear(
                        employee,
                        leaveType,
                        year)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave balance not found."));

        return LeaveBalanceMapper.toResponse(leaveBalance);
    }

    /**
     * Gives a new hire this year's balances the moment they are created, pro-rated
     * to the months left in the year (joining month counts as a full month, rounded
     * to half days). Only paid types get a balance — unpaid leave is never tracked.
     */
    @Override
    public void createBalancesForNewEmployee(Employee employee) {

        int year = java.time.LocalDate.now().getYear();
        int monthsLeft = 12;

        if (employee.getJoiningDate() != null) {

            java.time.LocalDate joined =
                    new java.sql.Date(employee.getJoiningDate().getTime()).toLocalDate();

            if (joined.getYear() > year) {
                // Joins in a future year; the accrual job will credit them then.
                return;
            }

            if (joined.getYear() == year) {
                monthsLeft = 12 - joined.getMonthValue() + 1;
            }
        }

        for (LeaveType leaveType : leaveTypeRepository.findAll()) {

            if (!leaveType.isPaid()) {
                continue;
            }

            if (leaveBalanceRepository
                    .findByEmployeeAndLeaveTypeAndYear(employee, leaveType, year)
                    .isPresent()) {
                continue;
            }

            double entitled =
                    Math.round(leaveType.getMaxDaysPerYear() * monthsLeft / 12.0 * 2) / 2.0;

            LeaveBalance balance = new LeaveBalance();
            balance.setEmployee(employee);
            balance.setLeaveType(leaveType);
            balance.setYear(year);
            balance.setTotalEntitled(entitled);
            balance.setUsed(0.0);

            leaveBalanceRepository.save(balance);
        }
    }

    @Override
    public List<LeaveUtilizationDto> getUtilizationByLeaveType(Integer year) {
        List<LeaveUtilizationDto> result = new java.util.ArrayList<>();
        for (Object[] row : leaveBalanceRepository.getUtilizationByLeaveType(year)) {
            String leaveTypeName = row[0].toString();
            Double totalEntitled = (Double) row[1];
            Double totalUsed = (Double) row[2];
            result.add(new LeaveUtilizationDto(
                    leaveTypeName,
                    totalEntitled,
                    totalUsed,
                    totalEntitled - totalUsed
            ));
        }
        return result;
    }

}

