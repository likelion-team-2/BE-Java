package com.example.demo.controller;

import com.example.demo.dto.request.ChangePasswordRequestDTO;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
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
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.request.UserRequestDTO;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/v1/api/user")
@Validated
@Slf4j
@Tag(name = "User", description = "The User API")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @Operation(summary = "Change password", description = "Change the user's password",  responses = {
            @ApiResponse(responseCode = "200", description = "Password changed in database",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "password changed successfully", summary = "return true",
                                    value = "{\"status\": 200, \"message\": \"Password changed successfully\", \"data\": \"true\"}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Old password and new password is invalid",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "bad request", summary = "old password and new password is invalid",
                                    value = "{status: 400, message: \"Password must be at least 8 characters\", data: \"1\"}<br />" +
                                            "{status: 400, message: \"New password must be at least 8 characters\", data: \"2\" }<br />"
                            ))),
            @ApiResponse(responseCode = "400", description = "Old password is incorrect",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "unauthorized", summary = "old password is incorrect",
                                    value = "{status: 401, message: \"Old password is incorrect\", data: \"3\"}<br />"

                            ))),
            @ApiResponse(responseCode = "409", description = "Old password and new password is same",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "conflict", summary = "old password and new password is same",
                                    value = "{status: 409, message: \"Old password and new password can't be the same\", data: \"4\" }<br />"
                            )))
    })
    @PatchMapping("/changepassword")
    public ResponseEntity<ResponseData<String>> changePassword (
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest,
            String email) throws NoSuchAlgorithmException, InvalidKeySpecException {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Password changed successfully", "true"));
    }
}
