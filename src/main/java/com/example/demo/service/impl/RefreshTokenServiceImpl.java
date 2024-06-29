package com.example.demo.service.impl;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.entities.User;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            throw new TokenRefreshException(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token", "1");
        }

        User user = userOpt.get();
        return userAuthProvider.createAccessToken(user.getUsername());
    }
}
