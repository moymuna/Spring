package com.emranhss.HRM_system.salarygrade;

import java.math.BigDecimal;

public class SalaryGradeMapper {

    public static SalaryGrade toEntity(SalaryGradeRequestDto dto) {

        SalaryGrade grade = new SalaryGrade();

        applyRequest(grade, dto);

        return grade;
    }

    public static void updateEntity(SalaryGrade grade, SalaryGradeRequestDto dto) {

        applyRequest(grade, dto);
    }

    private static void applyRequest(SalaryGrade grade, SalaryGradeRequestDto dto) {

        grade.setGradeNumber(dto.getGradeNumber());
        grade.setTitle(dto.getTitle());

        grade.setBasicSalary(nz(dto.getBasicSalary()));
        grade.setHra(nz(dto.getHra()));
        grade.setConveyanceAllowance(nz(dto.getConveyanceAllowance()));
        grade.setMedicalAllowance(nz(dto.getMedicalAllowance()));
        grade.setSpecialAllowance(nz(dto.getSpecialAllowance()));

        grade.setProvidentFund(nz(dto.getProvidentFund()));
        grade.setProfessionalTax(nz(dto.getProfessionalTax()));
        grade.setIncomeTax(nz(dto.getIncomeTax()));

        grade.setActive(dto.getActive() == null || dto.getActive());
    }

    public static SalaryGradeResponseDto toResponse(SalaryGrade grade) {

        SalaryGradeResponseDto dto = new SalaryGradeResponseDto();

        dto.setId(grade.getId());
        dto.setGradeNumber(grade.getGradeNumber());
        dto.setTitle(grade.getTitle());

        dto.setBasicSalary(grade.getBasicSalary());
        dto.setHra(grade.getHra());
        dto.setConveyanceAllowance(grade.getConveyanceAllowance());
        dto.setMedicalAllowance(grade.getMedicalAllowance());
        dto.setSpecialAllowance(grade.getSpecialAllowance());

        dto.setProvidentFund(grade.getProvidentFund());
        dto.setProfessionalTax(grade.getProfessionalTax());
        dto.setIncomeTax(grade.getIncomeTax());

        dto.setGrossMonthly(grade.getGrossMonthly());
        dto.setTotalDeductions(grade.getTotalDeductions());
        dto.setNetMonthly(grade.getGrossMonthly().subtract(grade.getTotalDeductions()));

        dto.setActive(grade.isActive());

        return dto;
    }

    private static BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
