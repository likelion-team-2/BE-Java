package com.example.demo.config;

import com.example.demo.entities.User;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.jwt-expiration-ms}")
    private int jwtExpirationMs;
    @Value("${security.jwt.token.jwt-refresh-expiration-ms}")
    private Long refreshTokenDurationMs;

    private final UserRepository userRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Generate access JWT token from the username
     * @param username The value need to be encoded to token
     * @return
     */
    public String createAccessToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationMs);

        return JWT.create()
                .withIssuer(username)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(secretKey));
    }

    /**
     * Generate referesh JWT token for the user id
     * @param userId The user id
     * @return
     */
    public String createRefreshToken(UUID userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenDurationMs);

        return JWT.create()
                .withIssuer(userId.toString())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(secretKey));
    }

    /**
     * Validate the token against the current user
     * @param token
     * @return
     */
    public Authentication validateAccessToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decoded = verifier.verify(token);

        Optional<User> user = userRepository.findByUsername(decoded.getIssuer());
        if (user.isPresent()) {
            return new UsernamePasswordAuthenticationToken(user.get(), null, Collections.emptyList());
        }
        return null;
    }

    /**
     * Get User from refresh token
     * @param token
     * @return
     */
    public Optional<User> getUserFromRefreshToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decoded = verifier.verify(token);

        // Throw exception if token is expired
        Date now = new Date();
        if (decoded.getExpiresAt().before(now)) {
            throw new TokenRefreshException(token, "Refresh token expired");
        }
        // Throw exception if token is invalid
        if (ObjectUtils.isEmpty(decoded.getIssuer())) {
            throw new TokenRefreshException(token, "Invalid refresh token");
        }

        Optional<User> user = userRepository.findById(UUID.fromString(decoded.getIssuer()));
        return user;
    }
}
