package com.example.demo.controller;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.dto.response.UserAuthResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.service.impl.OtpServiceImpl;
import com.example.demo.service.impl.RefreshTokenServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.response.TokenRefreshResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "User", description = "The User API")
public class AuthController {

    private final UserServiceImpl userService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final OtpServiceImpl otpService;

    @Operation(summary = "Add a new user", description = "Add a new user to the system",  responses = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
                                    value = "{\"status\": 200, \"message\": \"User created\", \"data\": \"true\"}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "TBD", summary = "TBD",
                                    value = "{status: 400, message: \"Can't create username with characters like admin\", data: \"2\"}<br />" +
                                            "{status: 400, message: \"Password must be at least 8 characters\", data: \"3\" }<br />" +
                                            "{status: 400, message: \"Can't create nickname with characters like admin\", data: \"4\"}<br />"
                            ))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "TBD", summary = "TBD",
                                    value = "{status: 409, message: \"UserName already exist\", data: \"1\" }<br />" +
                                            "{status: 409, message: \"Email already exist\", data: \"5\"}<br />"
                            )))
    })
    @PostMapping("/signup")
    public ResponseEntity<ResponseData<String>> signUp(@Valid @RequestBody UserRequestDTO userDTO) {
        // Logic to create user
        String data = userService.signUp(userDTO);
        ResponseData<String> responseData = new ResponseData<>(HttpStatus.OK.value(), "User created", data);
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Sign in user", description = "Sign in user to the system",  responses = {
            @ApiResponse(responseCode = "200", description = "User signed in",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "Login success", summary = "Data response example when login successful.",
                                    value = "{\"status\": 200, \"message\": \"User signed in\", " +
                                            "\"data\": \"{ accessToken, refreshToken, " +
                                            "user: { userId, username, email, nickname, regionCountry}}\"}"
                            ))),
            @ApiResponse(responseCode = "404", description = "Username or email is not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "User not found", summary = "Data response example when user not found.",
                                    value = "{status: 404, \"message\": \"User not found\", \"data\": \"1\"}<br /> " +
                                            "{status: 404, \"message\": \"Invalid password\", \"data\": \"2\"}<br />"
                            )))
    })
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
                            ))),
            @ApiResponse(responseCode = "401", description = "Denied token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "Invalid token", summary = "TBD",
                                    value = "{status: 401, message: \"Invalid refresh token\", data: \"1\" }<br />" +
                                            "{status: 401, message: \"Refresh token was expired\", data: \"2\"<br />}"
                            ))),})
    @PostMapping("/refreshtoken")
    public ResponseData<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequestDTO request) {
        String refreshToken = request.getRefreshToken();
        String newAccessToken = refreshTokenService.generateAccessTokenFromRefreshToken(refreshToken);

        return new ResponseData<>(HttpStatus.OK.value(), "Get refresh token successfully.",
                new TokenRefreshResponse(newAccessToken, refreshToken));
    }

    @Operation(summary = "Send OTP", description = "Send OTP to user",  responses = {
            @ApiResponse(responseCode = "200", description = "OTP sent",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
                                    value = "{\"status\": 200, \"message\": \"OTP sent\", \"data\": \"success\"}"
                            )))})
    @PostMapping("/sendotp")
    public ResponseEntity<ResponseData<String>> sendOtp(@Valid @RequestBody SendOtpRequestDTO sendOtpRequestDTO){
        // Logic to send OTP
        String otp = otpService.generateOtp(sendOtpRequestDTO);
        ResponseData<String> responseData = new ResponseData<>(HttpStatus.OK.value(), "OTP sent", otp);
        return ResponseEntity.ok(responseData);
    }

    @Operation (summary = "Verify OTP and change password", description = "Verify OTP and change password",  responses = {
            @ApiResponse(responseCode = "200", description = "Change password for forgotten password successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
                                    value = "{\"status\": 200, \"message\": \"Change password for forgotten password successfully\", \"data\": \"true\"}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "TBD", summary = "TBD",
                                    value = "{status: 400, message: \"Password must be at least 8 characters\", data: \"1\"}<br />" +
                                            "{status: 400, message: \"New password must be at least 8 characters\", data: \"2\"}<br />" +
                                            "{status: 400, message: \"Old password is incorrect\", data: \"3\"}<br />"
                            )))
    })
    @PostMapping("/verifyotpandchangepassword")
    public ResponseEntity<ResponseData<String>> verifyOtp(@Valid @RequestBody VerifyOTPAndChangePasswordRequestDTO verifyOTPAndChangePasswordRequestDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Logic to send OTP
        String result = userService.verifyOtpAndChangePassword(verifyOTPAndChangePasswordRequestDTO);
        ResponseData<String> responseData = new ResponseData<>(HttpStatus.OK.value(), "Change password for forgotten password successfully ", result);
        return ResponseEntity.ok(responseData);
    }
}
