package com.example.demo.controller;

import com.example.demo.dto.request.TokenRefreshRequestDTO;
import com.example.demo.dto.request.UserRequestSignInDTO;
import com.example.demo.dto.response.UserAuthResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.entities.RefreshToken;
import com.example.demo.entities.User;
import com.example.demo.service.impl.RefreshTokenServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.dto.response.TokenRefreshResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Sign in user", description = "Sign in user to the system",  responses = {
            @ApiResponse(responseCode = "200", description = "User signed in",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
                                    value = "{\"status\": 200, \"message\": \"User signed in\", " +
                                            "\"data\": \"{ accessToken, refreshToken, userId, username, email}\"}"
                            ))),})
    @PostMapping("/signin")
    public ResponseData<UserAuthResponse> signIn(@Valid @RequestBody UserRequestSignInDTO userRequestSignInDTO)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        UserAuthResponse userAuthResponse = userService.signIn(userRequestSignInDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Login successfully", userAuthResponse);
    }


    @Operation(summary = "Refresh token", description = "Get new access token from refesh token",  responses = {
            @ApiResponse(responseCode = "200", description = "Get token successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
                                    value = "{\"status\": 200, \"message\": \"Get refresh token successfully\"," +
                                            " \"data\": \"{ accessToken, refreshToken}\"}"
                            ))),})
    @PostMapping("/refreshtoken")
    public ResponseData<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        User user = refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is invalid!"));
        String accessToken = jwtUtil.generateAccessTokenFromUsername(user.getUsername());
        return new ResponseData<>(HttpStatus.OK.value(), "Get refresh token successfully.",
                new TokenRefreshResponse(accessToken, requestRefreshToken));
    }
}
