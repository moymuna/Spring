package com.emranhss.HRM_system.location.district;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/district")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DistrictResponseDto> createDistrict(
            @RequestBody DistrictRequestDto requestDto) {


        DistrictResponseDto response =
                districtService.createDistrict(requestDto);


        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DistrictResponseDto>> getAllDistricts() {


        List<DistrictResponseDto> districts =
                districtService.getAllDistricts();


        return ResponseEntity.ok(districts);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DistrictResponseDto> getDistrictById(
            @PathVariable Long id) {


        DistrictResponseDto district =
                districtService.getDistrictById(id);


        return ResponseEntity.ok(district);
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DistrictResponseDto> updateDistrict(
            @PathVariable Long id,
            @RequestBody DistrictRequestDto requestDto) {


        DistrictResponseDto updatedDistrict =
                districtService.updateDistrict(id, requestDto);


        return ResponseEntity.ok(updatedDistrict);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDistrict(
            @PathVariable Long id) {


        districtService.deleteDistrict(id);


        return ResponseEntity.noContent().build();
    }


    
    @GetMapping("/division/{divisionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DistrictResponseDto>> getDistrictsByDivisionId(
            @PathVariable Long divisionId) {


        List<DistrictResponseDto> districts =
                districtService.getDistrictsByDivisionId(divisionId);


        return ResponseEntity.ok(districts);
    }

    
    @GetMapping("/division-name/{divisionName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DistrictResponseDto>> getDistrictsByDivisionName(
            @PathVariable String divisionName) {


        List<DistrictResponseDto> districts =
                districtService.getDistrictsByDivisionName(divisionName);


        return ResponseEntity.ok(districts);
    }

}
