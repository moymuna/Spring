package com.emranhss.HRM_system.payslip;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.payroll.Payroll;
import com.emranhss.HRM_system.payroll.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class payslipServiceImpl implements PayslipService {
    private final PayslipRepository payslipRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;

    
    @Override
    public PayslipResponseDto createPayslip(PayslipRequestDto dto) {

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id : " + dto.getEmployeeId()));

        
        Payroll payroll = payrollRepository.findById(dto.getPayrollId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payroll not found with id : " + dto.getPayrollId()));

        
        Payslip payslip = PayslipMapper.toEntity(dto);

        
        payslip.setEmployee(employee);
        payslip.setPayroll(payroll);

        
        Payslip savedPayslip = payslipRepository.save(payslip);

        
        return PayslipMapper.toResponse(savedPayslip);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<PayslipResponseDto> getAllPayslips() {

        return payslipRepository.findAll()
                .stream()
                .map(PayslipMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PayslipResponseDto getPayslipById(Long id) {

        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payslip not found with id : " + id));

        return PayslipMapper.toResponse(payslip);
    }

    
    @Override
    public PayslipResponseDto updatePayslip(Long id,
                                            PayslipRequestDto dto) {

        
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payslip not found with id : " + id));

        
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id : "
                                + dto.getEmployeeId()));

        
        Payroll payroll = payrollRepository.findById(dto.getPayrollId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payroll not found with id : "
                                + dto.getPayrollId()));

        
        PayslipMapper.updateEntity(payslip, dto);

        
        payslip.setEmployee(employee);
        payslip.setPayroll(payroll);

        
        Payslip updatedPayslip = payslipRepository.save(payslip);

        return PayslipMapper.toResponse(updatedPayslip);
    }

    
    @Override
    public void deletePayslip(Long id) {

        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payslip not found with id : " + id));

        payslipRepository.delete(payslip);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<PayslipResponseDto> getPayslipsByEmployeeId(Long employeeId) {

        return payslipRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(PayslipMapper::toResponse)
                .collect(Collectors.toList());
    }
}
