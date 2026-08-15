package com.emranhss.HRM_system.user;

import com.emranhss.HRM_system.enums.Role;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;

    private String fullName;

    private String email;

    private Role role;

    private Boolean enabled;

    private Boolean accountLocked;

    private String photoPath;

    private String signaturePath;
}
