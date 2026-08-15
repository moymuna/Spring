package com.emranhss.HRM_system.advance;

import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.enums.AdvanceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AdvanceMapper {

    public static Advance toEntity(AdvanceRequestDto dto, Employee employee) {

        Advance advance = new Advance();

        applyRequest(advance, dto, employee);

        advance.setRecoveredAmount(BigDecimal.ZERO);

        // A request always starts as PENDING. Only the approve/reject/pay endpoints
        // move it on, so a status supplied by the caller is deliberately ignored.
        advance.setStatus(AdvanceStatus.PENDING);

        return advance;
    }

    /** Editing a request never changes its status — the decision endpoints own that. */
    public static void updateEntity(Advance advance, AdvanceRequestDto dto, Employee employee) {

        applyRequest(advance, dto, employee);
    }

    private static void applyRequest(Advance advance, AdvanceRequestDto dto, Employee employee) {

        advance.setAmount(dto.getAmount());
        advance.setRequestDate(dto.getRequestDate() == null ? LocalDate.now() : dto.getRequestDate());
        advance.setRequiredByDate(dto.getRequiredByDate());
        advance.setInstallments(dto.getInstallments() == null || dto.getInstallments() < 1
                ? 1
                : dto.getInstallments());
        advance.setReason(dto.getReason());

        advance.setEmployee(employee);
    }

    public static AdvanceResponseDto toResponse(Advance advance) {

        AdvanceResponseDto dto = new AdvanceResponseDto();

        dto.setId(advance.getId());
        dto.setAmount(advance.getAmount());
        dto.setRequestDate(advance.getRequestDate());
        dto.setRequiredByDate(advance.getRequiredByDate());
        dto.setInstallments(advance.getInstallments());
        dto.setMonthlyDeduction(advance.getMonthlyDeduction());
        dto.setRecoveredAmount(advance.getRecoveredAmount());
        dto.setOutstandingAmount(advance.getOutstandingAmount());
        dto.setReason(advance.getReason());
        dto.setStatus(advance.getStatus() == null ? null : advance.getStatus().name());
        dto.setDecidedAt(advance.getDecidedAt());
        dto.setRejectionReason(advance.getRejectionReason());

        if (advance.getEmployee() != null) {

            dto.setEmployeeId(advance.getEmployee().getId());
            dto.setEmployeeCode(advance.getEmployee().getEmployeeCode());

            if (advance.getEmployee().getUser() != null) {
                dto.setEmployeeName(advance.getEmployee().getUser().getFullName());
            }
        }

        return dto;
    }
}
