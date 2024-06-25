package com.example.demo.service;

import com.example.demo.dto.response.TokenRefreshResponse;
import com.example.demo.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    /**
     * Get refresh token by token string
     * @param token
     * @return refresh token
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Create new refresh token
     * @param userId
     * @return refresh token
     */
    RefreshToken createRefreshToken(Long userId);

    /**
     * Verify expiration of refresh token
     * @param token
     */
    RefreshToken verifyExpiration(RefreshToken token);

    /**
     * Delete refresh token by user id
     * Used when logout
     * @param userId
     */
    int deleteByUserId(Long userId);
}
