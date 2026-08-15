package com.emranhss.HRM_system.user;

public class UserMapper {


    
    public static User toEntity(UserRequestDto dto) {

        User user = new User();

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
        user.setAccountLocked(dto.getAccountLocked() != null ? dto.getAccountLocked() : false);
        user.setPhotoPath(dto.getPhotoPath());
        user.setSignaturePath(dto.getSignaturePath());

        return user;
    }

    
    public static UserResponseDto toResponse(User user) {

        UserResponseDto dto = new UserResponseDto();

        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setAccountLocked(user.isAccountLocked());
        dto.setPhotoPath(user.getPhotoPath());
        dto.setSignaturePath(user.getSignaturePath());

        return dto;
    }

    
    public static void updateEntity(User user,
                                    UserRequestDto dto) {

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());

        
        
        
        
        user.setPhotoPath(dto.getPhotoPath());
        user.setSignaturePath(dto.getSignaturePath());
    }

}
