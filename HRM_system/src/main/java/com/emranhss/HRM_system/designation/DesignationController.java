package com.emranhss.HRM_system.designation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designation")
@RequiredArgsConstructor
public class DesignationController {
    private final DesignationService designationService;

    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DesignationResponseDto> create(@RequestBody DesignationRequestDto dto) {

        return ResponseEntity.ok(designationService.createDesignation(dto));
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.hasAnyNonEmployeeRole()")
    public ResponseEntity<DesignationResponseDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok(designationService.getDesignationById(id));
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<DesignationResponseDto>> getAll() {

        return ResponseEntity.ok(designationService.getAllDesignations());
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DesignationResponseDto> update(
            @PathVariable Long id,
            @RequestBody DesignationRequestDto dto) {

        return ResponseEntity.ok(designationService.updateDesignation(id, dto));
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        designationService.deleteDesignation(id);
        return ResponseEntity.ok("Designation deleted successfully");
    }

    
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<DesignationResponseDto>> getByDepartment(
            @PathVariable Long departmentId) {

        return ResponseEntity.ok(designationService.getByDepartmentId(departmentId));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<DesignationResponseDto>> search(@RequestParam String keyword) {

        return ResponseEntity.ok(designationService.searchDesignations(keyword));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Long> count() {

        return ResponseEntity.ok(designationService.getDesignationCount());
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Page<DesignationResponseDto>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(designationService.getDesignations(pageable));
    }
}
