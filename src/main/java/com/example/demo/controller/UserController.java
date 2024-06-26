package com.example.demo.controller;

import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.dto.response.ResponseSuccess;
import com.example.demo.dto.response.UserSignInResponse;
import com.example.demo.service.impl.UserServiceImpl;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.request.UserRequestDTO;

@RestController
@RequestMapping("/v1/api/user")
@Validated
@Slf4j
@Tag(name = "User", description = "The User API")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    // TODO: Move to AuthController
    @Operation(summary = "Add a new user", description = "Add a new user to the system",  responses = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
                            value = "{\"status\": 200, \"message\": \"User created\", \"data\": \"user_id\"}"
                            ))),})
    @PostMapping("/signup")
    public ResponseData<UserSignInResponse> signUp(@Valid @RequestBody UserRequestDTO userDTO) {
        System.out.println("Received user signup request: " + Json.pretty(userDTO));
        try {
            // Logic to create user
//            userService.signUp(userDTO);

//            Long id = userService.signUp(userDTO);
            UserSignInResponse userSignInResponse = userService.signUp(userDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "User created", userSignInResponse );
        } catch (Exception e) {
            // Return error response
            return new ResponseError(HttpStatus.CONFLICT.value(), e.getMessage(), null);
        }
    }

    @Operation(summary = "Update user", description = "Update user in the system",  responses = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
                            value = "{\"status\": 200, \"message\": \"Updated successfully\", \"data\": \"User updated for: user_id\"}"
                            ))),})
    @PatchMapping("/updated/{user_id}")
    public ResponseSuccess updateUser(@Valid @NotBlank @PathVariable("user_id") String userId, @Valid @RequestBody UserRequestDTO userDTO) {
        System.out.println("Received user update request: " + userId);
        System.out.println("Received user update request: " + Json.pretty(userDTO));
        return new ResponseSuccess(HttpStatus.OK, "Updated successfully" ,"User updated for: " + userId);
    }

    @Operation(summary = "Delete user", description = "Delete user in the system",  responses = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
                            value = "{\"status\": 200, \"message\": \"User deleted\", \"data\": \"User deleted for: user_id\"}"
                            ))),})
    @DeleteMapping("/delete/{user_id}")
    public ResponseSuccess deleteUser(@Valid @PathVariable("user_id") String user_id) {
        System.out.println("Received user delete request: " + user_id);
        return new ResponseSuccess(HttpStatus.OK, "User deleted","User deleted for: " + user_id);
    }
}
