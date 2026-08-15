package com.emranhss.HRM_system.auth;

import com.emranhss.HRM_system.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private Role role;

}
