package com.emranhss.HRM_system.user;

import com.emranhss.HRM_system.auditlog.AuditAction;
import com.emranhss.HRM_system.auditlog.AuditLogService;
import com.emranhss.HRM_system.auth.RefreshTokenService;
import com.emranhss.HRM_system.exception.ConflictException;
import com.emranhss.HRM_system.exception.ResourceNotFoundException;
import com.emranhss.HRM_system.exception.ValidationException;
import com.emranhss.HRM_system.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final SecurityConfig encoder;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final RefreshTokenService refreshTokenService;

    
    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException(
                    "User already exists with email : " + dto.getEmail()
            );
        }

        if (dto.getPassword() == null || dto.getPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }

        
        User user = UserMapper.toEntity(dto);

        
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())

        );

        
        User savedUser = userRepository.save(user);

        auditLogService.record("User", savedUser.getId(), AuditAction.CREATE,
                "User " + savedUser.getEmail() + " created with role " + savedUser.getRole());

        
        return UserMapper.toResponse(savedUser);
    }

    
    @Override
    public List<UserResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public UserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        return UserMapper.toResponse(user);
    }

    
    @Override
    public UserResponseDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email : " + email
                        ));

        return UserMapper.toResponse(user);
    }

    
    @Override
    public UserResponseDto updateUser(Long id,
                                      UserRequestDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        
        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {

            throw new ConflictException(
                    "Email already exists : " + dto.getEmail()
            );
        }

        
        UserMapper.updateEntity(user, dto);

        

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    
    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        userRepository.delete(user);

        auditLogService.record("User", id, AuditAction.DELETE, "User " + user.getEmail() + " deleted");
    }

    
    @Override
    public UserResponseDto assignRole(Long id,
                                      UserRequestDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        if (dto.getRole() == null) {
            throw new ValidationException("Role is required.");
        }

        String previousRole = user.getRole().name();
        user.setRole(dto.getRole());

        UserResponseDto response = UserMapper.toResponse(
                userRepository.save(user)
        );

        auditLogService.record("User", id, AuditAction.ROLE_CHANGE,
                previousRole + " -> " + dto.getRole());

        return response;
    }

    
    @Override
    public UserResponseDto activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        user.setEnabled(true);

        UserResponseDto response = UserMapper.toResponse(
                userRepository.save(user)
        );

        auditLogService.record("User", id, AuditAction.ACTIVATE, user.getEmail() + " activated");

        return response;
    }

    
    @Override
    public UserResponseDto deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        user.setEnabled(false);
        user.setTokenVersion(user.getTokenVersion() + 1);
        refreshTokenService.revokeAllForUser(user);

        auditLogService.record("User", id, AuditAction.DEACTIVATE, user.getEmail() + " deactivated");

        return UserMapper.toResponse(
                userRepository.save(user)
        );
    }

    
    @Override
    public void resetPassword(Long id,
                              UserRequestDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        if (dto.getNewPassword() == null || dto.getNewPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }

        user.setPassword(
                passwordEncoder.encode(
                        dto.getNewPassword()
                )
        );

        userRepository.save(user);
    }

    
    @Override
    public void changePassword(Long id,
                               UserRequestDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        
        if (!encoder.passwordEncoder().matches(dto.getOldPassword(), user.getPassword())) {
            throw new ValidationException("Old password is incorrect.");
        }

        if (dto.getNewPassword() == null || dto.getNewPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }

        
        user.setPassword(
                passwordEncoder.encode(
                        dto.getNewPassword()
                )
        );

        userRepository.save(user);
    }

}
