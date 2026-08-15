package com.emranhss.HRM_system.payslip;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PayslipService {
    
    PayslipResponseDto createPayslip(PayslipRequestDto dto);

    
    List<PayslipResponseDto> getAllPayslips();

    
    PayslipResponseDto getPayslipById(Long id);

    
    PayslipResponseDto updatePayslip(Long id, PayslipRequestDto dto);

    
    void deletePayslip(Long id);

    
    List<PayslipResponseDto> getPayslipsByEmployeeId(Long employeeId);
}
