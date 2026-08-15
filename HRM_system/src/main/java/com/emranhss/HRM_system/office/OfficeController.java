package com.emranhss.HRM_system.office;

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
@RequestMapping("/api/office")
@RequiredArgsConstructor
public class OfficeController {
    private final OfficeService officeService;

    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfficeResponseDto> createOffice(@RequestBody OfficeRequestDto officeRequestDto) {
        OfficeResponseDto createdOffice = officeService.create(officeRequestDto);
        return new ResponseEntity<>(createdOffice, HttpStatus.CREATED);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<OfficeResponseDto> getOfficeById(@PathVariable Long id) {
        OfficeResponseDto office = officeService.getById(id);
        return ResponseEntity.ok(office);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<OfficeResponseDto>> getAllOffices() {
        List<OfficeResponseDto> offices = officeService.getAll();
        return ResponseEntity.ok(offices);
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfficeResponseDto> updateOffice(
            @PathVariable Long id,
            @RequestBody OfficeRequestDto officeRequestDto) {
        OfficeResponseDto updatedOffice = officeService.update(id, officeRequestDto);
        return ResponseEntity.ok(updatedOffice);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        officeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<OfficeResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(officeService.search(keyword));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(officeService.getOfficeCount());
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Page<OfficeResponseDto>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(officeService.getOffices(pageable));
    }
}
