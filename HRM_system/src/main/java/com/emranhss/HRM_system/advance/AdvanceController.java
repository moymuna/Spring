package com.emranhss.HRM_system.advance;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/advances")
@RequiredArgsConstructor
public class AdvanceController {

    private final AdvanceService advanceService;


    /** An employee may raise a request for themselves; staff may raise one for anybody. */
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER') or @employeeSecurity.isOwnerOrNotEmployee(#dto.employeeId)")
    public AdvanceResponseDto saveAdvance(
            @RequestBody AdvanceRequestDto dto) {

        return advanceService.saveAdvance(dto);
    }


    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isAdvanceOwnerOrNotEmployee(#id)")
    public AdvanceResponseDto getAdvanceById(
            @PathVariable Long id) {

        return advanceService.getAdvanceById(id);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<AdvanceResponseDto> getAllAdvances() {

        return advanceService.getAllAdvances();
    }


    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public List<AdvanceResponseDto> getAdvancesByEmployee(
            @PathVariable Long employeeId) {

        return advanceService.getAdvancesByEmployee(employeeId);
    }


    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<AdvanceResponseDto> getAdvancesByStatus(
            @PathVariable String status) {

        return advanceService.getAdvancesByStatus(status);
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("@employeeSecurity.isAdvanceOwnerOrNotEmployee(#id)")
    public AdvanceResponseDto updateAdvance(
            @PathVariable Long id,
            @RequestBody AdvanceRequestDto dto) {

        return advanceService.updateAdvance(id, dto);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String deleteAdvance(
            @PathVariable Long id) {

        advanceService.deleteAdvance(id);

        return "Advance request deleted successfully.";
    }


    @PutMapping("/approve/{advanceId}")
    @PreAuthorize("@employeeSecurity.canDecideOnAdvance(#advanceId)")
    public AdvanceResponseDto approveAdvance(
            @PathVariable Long advanceId) {

        return advanceService.approveAdvance(advanceId);
    }


    @PutMapping("/reject/{advanceId}")
    @PreAuthorize("@employeeSecurity.canDecideOnAdvance(#advanceId)")
    public AdvanceResponseDto rejectAdvance(
            @PathVariable Long advanceId,
            @RequestParam String rejectionReason) {

        return advanceService.rejectAdvance(advanceId, rejectionReason);
    }


    /** Disbursement and recovery are payroll actions, so they stay with ADMIN/HR. */
    @PutMapping("/pay/{advanceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public AdvanceResponseDto markAsPaid(
            @PathVariable Long advanceId) {

        return advanceService.markAsPaid(advanceId);
    }


    @PutMapping("/recover/{advanceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public AdvanceResponseDto recordRecovery(
            @PathVariable Long advanceId,
            @RequestParam BigDecimal amount) {

        return advanceService.recordRecovery(advanceId, amount);
    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public List<AdvanceResponseDto> search(
            @RequestParam String keyword) {

        return advanceService.searchAdvances(keyword);
    }


    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public Long count() {

        return advanceService.getAdvanceCount();
    }


    @GetMapping("/count/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public Long pendingCount() {

        return advanceService.getPendingCount();
    }


    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public Page<AdvanceResponseDto> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return advanceService.getAdvances(pageable);
    }
}
