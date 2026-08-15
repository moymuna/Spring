package com.emranhss.HRM_system.salary;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.salarygrade.SalaryGrade;

import java.math.BigDecimal;

public class SalaryMapper {


    public static Salary toEntity(SalaryRequestDto dto, Employee employee, SalaryGrade grade) {

        Salary salary = new Salary();

        applyRequest(salary, dto, employee, grade);

        return salary;
    }


    public static SalaryResponseDto toResponse(Salary salary) {

        SalaryResponseDto dto = new SalaryResponseDto();


        dto.setId(salary.getId());

        dto.setBasicSalary(salary.getBasicSalary());
        dto.setHra(salary.getHra());
        dto.setConveyanceAllowance(salary.getConveyanceAllowance());
        dto.setMedicalAllowance(salary.getMedicalAllowance());
        dto.setSpecialAllowance(salary.getSpecialAllowance());

        dto.setProvidentFund(salary.getProvidentFund());
        dto.setProfessionalTax(salary.getProfessionalTax());
        dto.setIncomeTax(salary.getIncomeTax());

        dto.setEffectiveFrom(salary.getEffectiveFrom());
        dto.setEffectiveTo(salary.getEffectiveTo());

        dto.setActive(salary.isActive());


        dto.setGrossMonthly(salary.getGrossMonthly());
        dto.setTotalDeductions(salary.getTotalDeductions());
        dto.setNetMonthly(salary.getGrossMonthly().subtract(salary.getTotalDeductions()));


        if (salary.getEmployee() != null) {

            dto.setEmployeeId(salary.getEmployee().getId());

            dto.setEmployeeCode(salary.getEmployee().getEmployeeCode());

            dto.setEmployeeName(salary.getEmployee().getUser().getFullName());
        }


        if (salary.getSalaryGrade() != null) {

            dto.setSalaryGradeId(salary.getSalaryGrade().getId());
            dto.setGradeNumber(salary.getSalaryGrade().getGradeNumber());
            dto.setGradeTitle(salary.getSalaryGrade().getTitle());
        }

        return dto;
    }


    public static void updateEntity(Salary salary,
                                    SalaryRequestDto dto,
                                    Employee employee,
                                    SalaryGrade grade) {

        applyRequest(salary, dto, employee, grade);
    }

    /**
     * Copies the request onto the entity. Any component the request leaves blank
     * falls back to the selected grade, so picking a grade is enough to build a
     * complete structure.
     */
    private static void applyRequest(Salary salary,
                                     SalaryRequestDto dto,
                                     Employee employee,
                                     SalaryGrade grade) {

        salary.setBasicSalary(pick(dto.getBasicSalary(), grade == null ? null : grade.getBasicSalary()));
        salary.setHra(pick(dto.getHra(), grade == null ? null : grade.getHra()));
        salary.setConveyanceAllowance(pick(dto.getConveyanceAllowance(), grade == null ? null : grade.getConveyanceAllowance()));
        salary.setMedicalAllowance(pick(dto.getMedicalAllowance(), grade == null ? null : grade.getMedicalAllowance()));
        salary.setSpecialAllowance(pick(dto.getSpecialAllowance(), grade == null ? null : grade.getSpecialAllowance()));

        salary.setProvidentFund(pick(dto.getProvidentFund(), grade == null ? null : grade.getProvidentFund()));
        salary.setProfessionalTax(pick(dto.getProfessionalTax(), grade == null ? null : grade.getProfessionalTax()));
        salary.setIncomeTax(pick(dto.getIncomeTax(), grade == null ? null : grade.getIncomeTax()));

        salary.setEffectiveFrom(dto.getEffectiveFrom());
        salary.setEffectiveTo(dto.getEffectiveTo());

        salary.setActive(dto.getActive() == null || dto.getActive());


        salary.setEmployee(employee);

        salary.setSalaryGrade(grade);
    }

    private static BigDecimal pick(BigDecimal requested, BigDecimal fromGrade) {

        if (requested != null) {
            return requested;
        }

        return fromGrade == null ? BigDecimal.ZERO : fromGrade;
    }
}
