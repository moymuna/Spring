package com.emranhss.HRM_system.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    
    UserResponseDto createUser(UserRequestDto dto);

    
    List<UserResponseDto> getAllUsers();

    
    UserResponseDto getUserById(Long id);

    
    UserResponseDto updateUser(Long id,
                               UserRequestDto dto);

    
    void deleteUser(Long id);

    
    UserResponseDto getUserByEmail(String email);

    UserResponseDto assignRole(Long id, UserRequestDto dto);

    UserResponseDto activateUser(Long id);

    UserResponseDto deactivateUser(Long id);

    void resetPassword(Long id, UserRequestDto dto);

    void changePassword(Long id, UserRequestDto dto);

}
