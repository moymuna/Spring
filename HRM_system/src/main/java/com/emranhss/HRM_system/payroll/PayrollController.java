package com.emranhss.HRM_system.payroll;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<PayrollResponseDto> createPayroll(@RequestBody PayrollRequestDto payrollRequestDto) {
        PayrollResponseDto createdPayroll = payrollService.createPayroll(payrollRequestDto);
        return new ResponseEntity<>(createdPayroll, HttpStatus.CREATED);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isPayrollOwnerOrNotEmployee(#id)")
    public ResponseEntity<PayrollResponseDto> getPayrollById(@PathVariable Long id) {
        PayrollResponseDto payroll = payrollService.getPayrollById(id);
        return ResponseEntity.ok(payroll);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<PayrollResponseDto>> getAllPayrolls() {
        List<PayrollResponseDto> payrolls = payrollService.getAllPayrolls();
        return ResponseEntity.ok(payrolls);
    }


    /** Payroll summary an employee can pull for themselves; staff can pull anyone's. */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public ResponseEntity<List<PayrollResponseDto>> getPayrollsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getPayrollsByEmployee(employeeId));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<PayrollResponseDto> updatePayroll(
            @PathVariable Long id,
            @RequestBody PayrollRequestDto payrollRequestDto) {
        PayrollResponseDto updatedPayroll = payrollService.updatePayroll(id, payrollRequestDto);
        return ResponseEntity.ok(updatedPayroll);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }

    /** Monthly salary sheet: one row per employee who has a salary structure. */
    @GetMapping("/sheet")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<SalarySheetRowDto>> salarySheet(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(payrollService.getSalarySheet(year, month));
    }


    /** Marks a generated payroll as paid (salary transferred from the company account). */
    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<PayrollResponseDto> payPayroll(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.payPayroll(id));
    }


    @PostMapping("/generate/{employeeId}/{year}/{month}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<PayrollResponseDto> generatePayroll(
            @PathVariable Long employeeId,
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(payrollService.generatePayroll(employeeId, month, year));
    }

    
    @GetMapping("/stats/monthly-cost")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<java.util.Map<Integer, java.math.BigDecimal>> monthlyCostTrend(@RequestParam int year) {
        return ResponseEntity.ok(payrollService.getMonthlyCostTrend(year));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<PayrollResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(payrollService.searchPayrolls(keyword));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(payrollService.getPayrollCount());
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Page<PayrollResponseDto>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(payrollService.getPayrolls(pageable));
    }
}
