package com.example.demo.service;

import com.example.demo.dto.response.TokenRefreshResponse;
import com.example.demo.entities.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    /**
     * Generate new access token from refresh token
     * @param refreshToken
     * @return
     */
    String generateAccessTokenFromRefreshToken(String refreshToken);

}
