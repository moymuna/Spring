package com.emranhss.HRM_system.payslip;

public class PayslipMapper {

    
    public static Payslip toEntity(PayslipRequestDto dto) {

        Payslip payslip = new Payslip();

        payslip.setMonth(dto.getMonth());
        payslip.setYear(dto.getYear());
        payslip.setGrossSalary(dto.getGrossSalary());
        payslip.setTotalDeductions(dto.getTotalDeductions());
        payslip.setNetSalary(dto.getNetSalary());
        payslip.setPaidDays(dto.getPaidDays());
        payslip.setLopDays(dto.getLopDays());
        payslip.setStatus(dto.getStatus());
        payslip.setGeneratedAt(dto.getGeneratedAt());
        payslip.setPaidAt(dto.getPaidAt());

        return payslip;
    }

    
    public static PayslipResponseDto toResponse(Payslip payslip) {

        PayslipResponseDto dto = new PayslipResponseDto();

        dto.setId(payslip.getId());
        dto.setMonth(payslip.getMonth());
        dto.setYear(payslip.getYear());
        dto.setGrossSalary(payslip.getGrossSalary());
        dto.setTotalDeductions(payslip.getTotalDeductions());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setPaidDays(payslip.getPaidDays());
        dto.setLopDays(payslip.getLopDays());
        dto.setStatus(payslip.getStatus());
        dto.setGeneratedAt(payslip.getGeneratedAt());
        dto.setPaidAt(payslip.getPaidAt());
        
        if (payslip.getEmployee() != null) {

            dto.setEmployeeId(payslip.getEmployee().getId());

            dto.setEmployeeName(payslip.getEmployee().getUser().getFullName());

            dto.setBankName(payslip.getEmployee().getBankName());
            dto.setBankAccountNumber(maskAccount(payslip.getEmployee().getBankAccountNumber()));
        }


        if (payslip.getPayroll() != null) {

            dto.setPayrollId(payslip.getPayroll().getId());

            // Held on the payroll rather than copied onto the payslip, so the
            // breakdown can never drift from the run it came from.
            dto.setUnpaidLeaveDays(payslip.getPayroll().getUnpaidLeaveDays());
            dto.setLeaveDeduction(payslip.getPayroll().getLeaveDeduction());
            dto.setAdvanceDeduction(payslip.getPayroll().getAdvanceDeduction());
            dto.setLopDeduction(payslip.getPayroll().getLopDeduction());
            dto.setProvidentFund(payslip.getPayroll().getProvidentFund());
            dto.setProfessionalTax(payslip.getPayroll().getProfessionalTax());
            dto.setIncomeTax(payslip.getPayroll().getIncomeTax());
        }

        return dto;
    }

    
    /** Payslips never carry the full account number — last four digits only. */
    private static String maskAccount(String accountNumber) {

        if (accountNumber == null || accountNumber.length() <= 4) {
            return accountNumber;
        }

        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }


    public static void updateEntity(Payslip payslip,
                                    PayslipRequestDto dto) {

        payslip.setMonth(dto.getMonth());
        payslip.setYear(dto.getYear());
        payslip.setGrossSalary(dto.getGrossSalary());
        payslip.setTotalDeductions(dto.getTotalDeductions());
        payslip.setNetSalary(dto.getNetSalary());
        payslip.setPaidDays(dto.getPaidDays());
        payslip.setLopDays(dto.getLopDays());
        payslip.setStatus(dto.getStatus());
        payslip.setGeneratedAt(dto.getGeneratedAt());
        payslip.setPaidAt(dto.getPaidAt());
    }
}
