package com.emranhss.HRM_system.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody UserRequestDto dto) {

        UserResponseDto response = userService.createUser(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isSelfOrStaff(#id)")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(
                userService.getUserByEmail(email)
        );
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isSelfOrStaff(#id)")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto dto) {

        UserResponseDto response =
                userService.updateUser(id, dto);

        return ResponseEntity.ok(response);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted successfully.");
    }
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> assignRole(
            @PathVariable Long id,
            @RequestBody UserRequestDto dto){

        return ResponseEntity.ok(
                userService.assignRole(id,dto)
        );

    }



    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> activateUser(
            @PathVariable Long id){

        return ResponseEntity.ok(
                userService.activateUser(id)
        );

    }



    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> deactivateUser(
            @PathVariable Long id){

        return ResponseEntity.ok(
                userService.deactivateUser(id)
        );

    }



    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> resetPassword(
            @PathVariable Long id,
            @RequestBody UserRequestDto dto){

        userService.resetPassword(id,dto);

        return ResponseEntity.ok(
                "Password reset successfully."
        );

    }



    @PutMapping("/{id}/change-password")
    @PreAuthorize("@employeeSecurity.isSelf(#id)")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @RequestBody UserRequestDto dto){

        userService.changePassword(id,dto);

        return ResponseEntity.ok(
                "Password changed successfully."
        );

    }

}
