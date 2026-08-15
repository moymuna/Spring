package com.emranhss.HRM_system.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emranhss.HRM_system.applicant.ApplicantRequestDto;
import com.emranhss.HRM_system.user.UserRequestDto;
import com.emranhss.HRM_system.user.UserResponseDto;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(authService.signup(dto));
    }

    
    
    @PostMapping("register-applicant")
    public ResponseEntity<UserResponseDto> registerApplicant(@RequestBody ApplicantRequestDto dto) {
        return ResponseEntity.ok(authService.registerApplicant(dto));
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest ar){
        return ResponseEntity.ok(authService.login(ar));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest dto) {
        return ResponseEntity.ok(authService.refreshAccessToken(dto.getRefreshToken()));
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest dto) {
        authService.logout(dto.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully.");
    }

    
    
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }

    
    
    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestBody ForgetPasswordRequest dto) {
        authService.sendVerificationEmail(dto.getEmail());
        return ResponseEntity.ok("Verification email sent to " + dto.getEmail());
    }

    

    
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgetPasswordRequest dto) {
        authService.forgotPassword(dto);
        return ResponseEntity.ok("Password reset link sent to " + dto.getEmail());
    }

    
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok("Password reset successful. You can now log in with your new password.");
    }

}
