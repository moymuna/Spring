package com.emranhss.HRM_system.payslip;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payslip")
@RequiredArgsConstructor
public class PayslipController {

    private final PayslipService payslipService;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<PayslipResponseDto> createPayslip(
            @RequestBody PayslipRequestDto dto) {

        PayslipResponseDto response = payslipService.createPayslip(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<PayslipResponseDto>> getAllPayslips() {

        return ResponseEntity.ok(payslipService.getAllPayslips());
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isPayslipOwnerOrNotEmployee(#id)")
    public ResponseEntity<PayslipResponseDto> getPayslipById(
            @PathVariable Long id) {

        return ResponseEntity.ok(payslipService.getPayslipById(id));
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<PayslipResponseDto> updatePayslip(
            @PathVariable Long id,
            @RequestBody PayslipRequestDto dto) {

        PayslipResponseDto response =
                payslipService.updatePayslip(id, dto);

        return ResponseEntity.ok(response);
    }

    
    @DeleteMapping ("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePayslip(
            @PathVariable Long id) {

        payslipService.deletePayslip(id);

        return ResponseEntity.ok("Payslip deleted successfully.");
    }

    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public ResponseEntity<List<PayslipResponseDto>> getPayslipsByEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                payslipService.getPayslipsByEmployeeId(employeeId)
        );
    }
}
