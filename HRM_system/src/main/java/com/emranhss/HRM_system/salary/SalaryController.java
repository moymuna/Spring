package com.emranhss.HRM_system.salary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<SalaryResponseDto> createSalary(
            @RequestBody SalaryRequestDto dto) {

        SalaryResponseDto response =
                salaryService.createSalary(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<SalaryResponseDto>> getAllSalaries() {

        List<SalaryResponseDto> response =
                salaryService.getAllSalaries();

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isSalaryOwnerOrNotEmployee(#id)")
    public ResponseEntity<SalaryResponseDto> getSalaryById(
            @PathVariable Long id) {

        SalaryResponseDto response =
                salaryService.getSalaryById(id);

        return ResponseEntity.ok(response);
    }


    /** An employee may read their own salary structure; staff may read anyone's. */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public ResponseEntity<SalaryResponseDto> getSalaryByEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(salaryService.getSalaryByEmployee(employeeId));
    }


    @GetMapping("/employee/{employeeId}/history")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public ResponseEntity<List<SalaryResponseDto>> getSalaryHistoryByEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(salaryService.getSalaryHistoryByEmployee(employeeId));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<SalaryResponseDto> updateSalary(
            @PathVariable Long id,
            @RequestBody SalaryRequestDto dto) {

        SalaryResponseDto response =
                salaryService.updateSalary(id, dto);

        return ResponseEntity.ok(response);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSalary(
            @PathVariable Long id) {

        salaryService.deleteSalary(id);

        return ResponseEntity.ok("Salary deleted successfully.");
    }
}
