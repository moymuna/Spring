package com.emranhss.HRM_system.auth;

import com.emranhss.HRM_system.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
