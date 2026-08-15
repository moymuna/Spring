package com.emranhss.HRM_system.payroll;

import com.emranhss.HRM_system.employee.Employee;

public class PayrollMapper {

    
    public static Payroll toEntity(PayrollRequestDto dto,
                                   Employee employee) {

        Payroll payroll = new Payroll();

        payroll.setMonth(dto.getMonth());
        payroll.setYear(dto.getYear());
        payroll.setGrossSalary(dto.getGrossSalary());
        payroll.setTotalDeductions(dto.getTotalDeductions());
        payroll.setNetSalary(dto.getNetSalary());
        payroll.setPaidDays(dto.getPaidDays());
        payroll.setLopDays(dto.getLopDays());
        payroll.setStatus(dto.getStatus());
        payroll.setGeneratedAt(dto.getGeneratedAt());
        payroll.setPaidAt(dto.getPaidAt());

        payroll.setEmployee(employee);

        return payroll;
    }

    
    public static PayrollResponseDto toResponse(Payroll payroll) {

        PayrollResponseDto dto = new PayrollResponseDto();

        dto.setId(payroll.getId());
        dto.setMonth(payroll.getMonth());
        dto.setYear(payroll.getYear());
        dto.setGrossSalary(payroll.getGrossSalary());
        dto.setTotalDeductions(payroll.getTotalDeductions());
        dto.setNetSalary(payroll.getNetSalary());
        dto.setPaidDays(payroll.getPaidDays());
        dto.setLopDays(payroll.getLopDays());
        dto.setUnpaidLeaveDays(payroll.getUnpaidLeaveDays());
        dto.setLeaveDeduction(payroll.getLeaveDeduction());
        dto.setAdvanceDeduction(payroll.getAdvanceDeduction());
        dto.setStatus(payroll.getStatus());
        dto.setGeneratedAt(payroll.getGeneratedAt());
        dto.setPaidAt(payroll.getPaidAt());

        if (payroll.getEmployee() != null) {
            dto.setEmployeeId(payroll.getEmployee().getId());
            dto.setEmployeeName(payroll.getEmployee().getUser().getFullName());
        }

        return dto;
    }

    
    public static void updateEntity(Payroll payroll,
                                    PayrollRequestDto dto,
                                    Employee employee) {

        payroll.setMonth(dto.getMonth());
        payroll.setYear(dto.getYear());
        payroll.setGrossSalary(dto.getGrossSalary());
        payroll.setTotalDeductions(dto.getTotalDeductions());
        payroll.setNetSalary(dto.getNetSalary());
        payroll.setPaidDays(dto.getPaidDays());
        payroll.setLopDays(dto.getLopDays());
        payroll.setStatus(dto.getStatus());
        payroll.setGeneratedAt(dto.getGeneratedAt());
        payroll.setPaidAt(dto.getPaidAt());

        payroll.setEmployee(employee);
    }
}
