package com.emranhss.HRM_system.department;

import com.emranhss.HRM_system.employee.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDto> createDepartment(
            @RequestBody DepartmentRequestDto dto) {

        return ResponseEntity.ok(departmentService.saveDepartment(dto));
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<DepartmentResponseDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<DepartmentResponseDto>> getAll() {

        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDto> update(
            @PathVariable Long id,
            @RequestBody DepartmentRequestDto dto) {

        return ResponseEntity.ok(departmentService.updateDepartment(id, dto));
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully");
    }

    
    @GetMapping("/{id}/employees")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<Employee>> getEmployees(@PathVariable Long id) {

        return ResponseEntity.ok(departmentService.getEmployeesByDepartment(id));
    }

    
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<DepartmentResponseDto> getByName(@PathVariable String name) {

        return ResponseEntity.ok(departmentService.getDepartmentByName(name));
    }

    
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<DepartmentResponseDto> getByCode(@PathVariable String code) {

        return ResponseEntity.ok(departmentService.getDepartmentByCode(code));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<DepartmentResponseDto>> search(@RequestParam String keyword) {

        return ResponseEntity.ok(departmentService.searchDepartments(keyword));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Long> count() {

        return ResponseEntity.ok(departmentService.getDepartmentCount());
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Page<DepartmentResponseDto>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(departmentService.getDepartments(pageable));
    }
}
