package com.emranhss.HRM_system.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
