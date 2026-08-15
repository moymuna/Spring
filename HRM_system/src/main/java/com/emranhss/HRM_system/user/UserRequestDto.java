package com.emranhss.HRM_system.user;

import com.emranhss.HRM_system.enums.Role;
import lombok.Data;

@Data

public class UserRequestDto {

    private String fullName;

    private String email;

    private String password;

    private Role role;

    private Boolean enabled;

    private Boolean accountLocked;

    private String oldPassword;

    private String newPassword;

    private String photoPath;

    private String signaturePath;

}

