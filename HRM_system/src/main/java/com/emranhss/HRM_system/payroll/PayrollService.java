package com.emranhss.HRM_system.payroll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PayrollService {

    PayrollResponseDto createPayroll(PayrollRequestDto payrollRequestDto);

    PayrollResponseDto getPayrollById(Long id);

    List<PayrollResponseDto> getAllPayrolls();

    List<PayrollResponseDto> getPayrollsByEmployee(Long employeeId);

    List<SalarySheetRowDto> getSalarySheet(int year, int month);

    PayrollResponseDto updatePayroll(Long id, PayrollRequestDto payrollRequestDto);

    void deletePayroll(Long id);

    List<PayrollResponseDto> searchPayrolls(String keyword);

    long getPayrollCount();

    Page<PayrollResponseDto> getPayrolls(Pageable pageable);

    PayrollResponseDto generatePayroll(Long employeeId, int month, int year);

    PayrollResponseDto payPayroll(Long id);

    
    java.util.Map<Integer, java.math.BigDecimal> getMonthlyCostTrend(int year);
}
