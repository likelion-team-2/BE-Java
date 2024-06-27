package com.example.demo.service.impl;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.entities.RefreshToken;
import com.example.demo.entities.User;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.repositories.RefreshTokenRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

   private final UserAuthProvider userAuthProvider;

    /**
     * ${inheritDoc}
     */
    @Override
    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        Optional<User> userOpt = userAuthProvider.getUserFromRefreshToken(refreshToken);
        if (userOpt.isEmpty()) {
            throw new TokenRefreshException(refreshToken, "Invalid refresh token");
        }

        User user = userOpt.get();
        return userAuthProvider.createAccessToken(user.getUsername());
    }
}
