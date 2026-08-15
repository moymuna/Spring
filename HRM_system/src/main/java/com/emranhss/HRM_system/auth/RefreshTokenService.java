package com.emranhss.HRM_system.auth;

import com.emranhss.HRM_system.exception.ValidationException;
import com.emranhss.HRM_system.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    public RefreshToken createRefreshToken(User user) {
        
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyAndConsume(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new ValidationException("Refresh token has expired. Please log in again.");
        }

        return refreshToken;
    }

    
    public Optional<User> revoke(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        refreshToken.ifPresent(refreshTokenRepository::delete);
        return refreshToken.map(RefreshToken::getUser);
    }

    
    public void revokeAllForUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
