package com.emranhss.HRM_system.security;


import com.emranhss.HRM_system.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; 

    @Value("${jwt.verification-expiration}")
    private long verificationExpiration; 

    @Value("${jwt.reset-expiration}")
    private long resetExpiration; 


    
    
    public String generateToken(String email, Role role, int tokenVersion) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .claim("tv", tokenVersion)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }


    
    
    
    public String generateResetToken(String email, int tokenVersion) {
        return Jwts.builder()
                .subject(email)
                .claim("purpose", "PASSWORD_RESET")
                .claim("tv", tokenVersion)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + resetExpiration))
                .signWith(getKey())
                .compact();
    }

    
    public String generateVerificationToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("purpose", "EMAIL_VERIFICATION")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + verificationExpiration))
                .signWith(getKey())
                .compact();
    }


    
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }


    
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    public String extractPurpose(String token) {
        return (String) getClaims(token).get("purpose");
    }

    
    public Integer extractTokenVersion(String token) {
        Object tv = getClaims(token).get("tv");
        return tv != null ? ((Number) tv).intValue() : null;
    }


    
    public boolean isValidForPurpose(String token, String expectedPurpose) {
        try {
            Claims claims = getClaims(token);
            return expectedPurpose.equals(claims.get("purpose"));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
