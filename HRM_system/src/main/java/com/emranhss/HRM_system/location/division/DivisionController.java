package com.emranhss.HRM_system.location.division;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/division")
public class DivisionController {

    private final DivisionService divisionService;


    public DivisionController(DivisionService divisionService) {
        this.divisionService = divisionService;
    }

    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DivisionResponseDto> saveDivision(
            @RequestBody DivisionRequestDto dto) {

        return new ResponseEntity<>(
                divisionService.saveDivision(dto),
                HttpStatus.CREATED
        );
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DivisionResponseDto> getDivisionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                divisionService.getDivisionById(id)
        );
    }

    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DivisionResponseDto>> getAllDivisions() {

        return ResponseEntity.ok(
                divisionService.getAllDivisions()
        );
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DivisionResponseDto> updateDivision(
            @PathVariable Long id,
            @RequestBody DivisionRequestDto dto) {

        return ResponseEntity.ok(
                divisionService.updateDivision(id, dto)
        );
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteDivision(
            @PathVariable Long id) {

        divisionService.deleteDivision(id);

        return ResponseEntity.ok("Division deleted successfully.");
    }

    
    @GetMapping("/country/{countryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DivisionResponseDto>> getDivisionsByCountryId(
            @PathVariable Long countryId) {

        return ResponseEntity.ok(
                divisionService.getDivisionsByCountryId(countryId)
        );
    }

    
    @GetMapping("/country/name/{countryName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DivisionResponseDto>> getDivisionsByCountryName(
            @PathVariable String countryName) {

        return ResponseEntity.ok(
                divisionService.getDivisionsByCountryName(countryName)
        );
    }
}
