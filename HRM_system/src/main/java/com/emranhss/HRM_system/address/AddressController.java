package com.emranhss.HRM_system.address;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<AddressResponseDto> createAddress(
            @RequestBody AddressRequestDto dto) {

        Address created = addressService.create(dto);

        return new ResponseEntity<>(
                addressMapper.toResponse(created),
                HttpStatus.CREATED
        );
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER') or @employeeSecurity.isAddressOwnerOrNotEmployee(#id)")
    public ResponseEntity<AddressResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getById(id));
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<AddressResponseDto>> getAll() {
        return ResponseEntity.ok(addressService.getAll());
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("@employeeSecurity.hasAnyNonEmployeeRole()")
    public AddressResponseDto update(
            @PathVariable Long id,
            @RequestBody AddressRequestDto dto) {

        Address address = addressService.update(id, dto);

        return addressMapper.toResponse(address);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("@employeeSecurity.hasAnyNonEmployeeRole()")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
