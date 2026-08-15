package com.emranhss.HRM_system.salarygrade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary-grades")
@RequiredArgsConstructor
public class SalaryGradeController {

    private final SalaryGradeService salaryGradeService;

    /** Only the admin maintains the pay scale — HR and everyone else can read it. */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaryGradeResponseDto> createGrade(
            @RequestBody SalaryGradeRequestDto dto) {

        return new ResponseEntity<>(salaryGradeService.createGrade(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<SalaryGradeResponseDto>> getAllGrades() {

        return ResponseEntity.ok(salaryGradeService.getAllGrades());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<SalaryGradeResponseDto>> getActiveGrades() {

        return ResponseEntity.ok(salaryGradeService.getActiveGrades());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<SalaryGradeResponseDto> getGradeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(salaryGradeService.getGradeById(id));
    }

    @GetMapping("/number/{gradeNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<SalaryGradeResponseDto> getGradeByNumber(
            @PathVariable Integer gradeNumber) {

        return ResponseEntity.ok(salaryGradeService.getGradeByNumber(gradeNumber));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaryGradeResponseDto> updateGrade(
            @PathVariable Long id,
            @RequestBody SalaryGradeRequestDto dto) {

        return ResponseEntity.ok(salaryGradeService.updateGrade(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteGrade(
            @PathVariable Long id) {

        salaryGradeService.deleteGrade(id);

        return ResponseEntity.ok("Salary grade deleted successfully.");
    }
}
