package com.emranhss.HRM_system.location.policestation;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policeStation")
@RequiredArgsConstructor
public class PoliceStationController {

    private final PoliceStationService policeStationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PoliceStationResponseDto> save(
            @RequestBody PoliceStationRequestDto dto) {


        PoliceStation saved =
                policeStationService.save(dto);


        return new ResponseEntity<>(
                PoliceStationMapper.toDTO(saved),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PoliceStationResponseDto>> getAll() {
        List<PoliceStation> list = policeStationService.findAll();
        return ResponseEntity.ok(list.stream().map(PoliceStationMapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PoliceStationResponseDto> getById(@PathVariable Long id) {

        PoliceStation policeStation =
                policeStationService.getById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Police Station Not Found"));

        return ResponseEntity.ok(PoliceStationMapper.toDTO(policeStation));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(
            @PathVariable Long id) {

        policeStationService.delete(id);

        return ResponseEntity.ok(
                "Police Station Deleted Successfully"
        );
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PoliceStationResponseDto> update(
            @PathVariable Long id,
            @RequestBody PoliceStationRequestDto dto) {


        PoliceStation updated =
                policeStationService.update(id, dto);


        return ResponseEntity.ok(
                PoliceStationMapper.toDTO(updated)
        );
    }


    @GetMapping("/district/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PoliceStationResponseDto>> getByDistrictId(@PathVariable Long id) {
        List<PoliceStationResponseDto> list = policeStationService.findByDistrictId(id);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/district/name/{name}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PoliceStationResponseDto>> getByCountryName(@PathVariable String name) {
        List<PoliceStationResponseDto> list = policeStationService.findByDistrictName(name);
        return ResponseEntity.ok(list);
    }


}
