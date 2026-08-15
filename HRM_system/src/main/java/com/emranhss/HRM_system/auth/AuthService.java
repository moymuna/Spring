package com.emranhss.HRM_system.auth;

import com.emranhss.HRM_system.applicant.Applicant;
import com.emranhss.HRM_system.applicant.ApplicantRepository;
import com.emranhss.HRM_system.applicant.ApplicantRequestDto;
import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.enums.Role;
import com.emranhss.HRM_system.exception.ConflictException;
import com.emranhss.HRM_system.exception.ExternalServiceException;
import com.emranhss.HRM_system.exception.ResourceNotFoundException;
import com.emranhss.HRM_system.exception.ValidationException;
import com.emranhss.HRM_system.security.JwtUtil;
import com.emranhss.HRM_system.user.User;
import com.emranhss.HRM_system.user.UserRepository;
import com.emranhss.HRM_system.utill.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emranhss.HRM_system.user.UserRequestDto;
import com.emranhss.HRM_system.user.UserResponseDto;
import com.emranhss.HRM_system.user.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final AuditLogService auditLogService;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public UserResponseDto signup(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("User already exists with email: " + dto.getEmail());
        }
        User user = UserMapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setEnabled(true); 
        user.setRole(Role.APPLICANT); 
        user.setAccountLocked(false);
        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

    
    @Transactional
    public UserResponseDto registerApplicant(ApplicantRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("User already exists with email: " + dto.getEmail());
        }
        if (applicantRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("An applicant profile already exists with email: " + dto.getEmail());
        }

        User user = new User();
        user.setFullName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(Role.APPLICANT);
        user.setEnabled(true);
        user.setAccountLocked(false);
        User savedUser = userRepository.save(user);

        Applicant applicant = new Applicant();
        applicant.setName(dto.getName());
        applicant.setEmail(dto.getEmail());
        applicant.setPhone(dto.getPhone());
        applicant.setAddress(dto.getAddress());
        applicant.setEducation(dto.getEducation());
        applicant.setExperience(dto.getExperience());
        applicant.setSkills(dto.getSkills());
        applicant.setUser(savedUser);
        applicantRepository.save(applicant);

        return UserMapper.toResponse(savedUser);
    }

    private static final int MAX_FAILED_ATTEMPTS = 8;

    public AuthResponse login(AuthRequest lr) {
        User user = userRepository.findByEmail(lr.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(lr.getEmail(), lr.getPassword())
            );
        } catch (BadCredentialsException ex) {
            registerFailedLogin(user);
            throw new BadCredentialsException("Invalid email or password");
        } catch (LockedException ex) {
            throw new LockedException("Account is locked due to too many failed login attempts. Contact an administrator.");
        } catch (DisabledException ex) {
            throw new DisabledException("Account is not verified. Please check your email.");
        }

        if (user.getFailedLoginAttempts() != 0) {
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getTokenVersion());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .fullName(user.getFullName())
                .token(token)
                .refreshToken(refreshToken.getToken())
                .build();

    }

    
    public AuthResponse refreshAccessToken(String refreshTokenValue) {
        RefreshToken existing = refreshTokenService.verifyAndConsume(refreshTokenValue);
        User user = existing.getUser();

        if (!user.isEnabled() || user.isAccountLocked()) {
            refreshTokenService.revokeAllForUser(user);
            throw new ValidationException("Account is locked or disabled. Please contact an administrator.");
        }

        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getTokenVersion());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .fullName(user.getFullName())
                .token(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    public void logout(String refreshTokenValue) {
        if (refreshTokenValue != null && !refreshTokenValue.isBlank()) {
            refreshTokenService.revoke(refreshTokenValue).ifPresent(user -> {
                
                
                
                user.setTokenVersion(user.getTokenVersion() + 1);
                userRepository.save(user);
            });
        }
    }

    private void registerFailedLogin(User user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        auditLogService.recordForActor(user.getEmail(), "User", user.getId(), AuditAction.LOGIN_FAILED,
                "Failed attempt " + user.getFailedLoginAttempts() + "/" + MAX_FAILED_ATTEMPTS);

        if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLocked(true);
            user.setTokenVersion(user.getTokenVersion() + 1);
            refreshTokenService.revokeAllForUser(user);
            auditLogService.recordForActor(user.getEmail(), "User", user.getId(), AuditAction.ACCOUNT_LOCKED,
                    "Locked after " + MAX_FAILED_ATTEMPTS + " consecutive failed login attempts");
        }
        userRepository.save(user);
    }


    
    public void sendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (user.isEnabled()) {
            throw new ConflictException("Account is already verified");
        }

        String token = jwtUtil.generateVerificationToken(user.getEmail());
        String link = frontendUrl + "/verify-email?token=" + token;

        try {
            emailService.sendSimpleMail(
                    user.getEmail(),
                    "Verify your email address",
                    "<p>Hi " + user.getFullName() + ",</p>"
                            + "<p>Please verify your email address by clicking the link below:</p>"
                            + "<p><a href=\"" + link + "\">" + link + "</a></p>"
                            + "<p>This link expires in 1 hour.</p>"
            );
        } catch (MessagingException e) {
            throw new ExternalServiceException("Failed to send verification email. Please try again shortly.");
        }
    }



    
    public void verifyEmail(String token) {

        if (!jwtUtil.isValidForPurpose(token, "EMAIL_VERIFICATION")) {
            throw new ValidationException("Invalid or expired verification link");
        }

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isEnabled()) {
            throw new ConflictException("Account is already verified");
        }

        user.setEnabled(true);
        userRepository.save(user);
    }

    
    
    
    public void forgotPassword(ForgetPasswordRequest dto) {

        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);
        if (user == null) {
            return;
        }

        String token = jwtUtil.generateResetToken(user.getEmail(), user.getTokenVersion());
        String link = frontendUrl + "/reset-password?token=" + token;

        try {
            emailService.sendSimpleMail(
                    user.getEmail(),
                    "Reset your password",
                    "<p>Hi " + user.getFullName() + ",</p>"
                            + "<p>We received a request to reset your password. Click the link below to choose a new one:</p>"
                            + "<p><a href=\"" + link + "\">" + link + "</a></p>"
                            + "<p>This link expires in 15 minutes. If you didn't request this, you can ignore this email.</p>"
            );
        } catch (MessagingException e) {
            throw new ExternalServiceException("Failed to send reset email. Please try again shortly.");
        }
    }

    
    public void resetPassword(ResetPasswordRequest dto) {

        if (!jwtUtil.isValidForPurpose(dto.getToken(), "PASSWORD_RESET")) {
            throw new ValidationException("Invalid or expired reset link");
        }

        if (dto.getNewPassword() == null || dto.getNewPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }

        String email = jwtUtil.extractEmail(dto.getToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        
        
        
        Integer tokenVersion = jwtUtil.extractTokenVersion(dto.getToken());
        if (tokenVersion == null || tokenVersion != user.getTokenVersion()) {
            throw new ValidationException("Invalid or expired reset link");
        }

        user.setPassword(encoder.encode(dto.getNewPassword()));
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
    }



}
