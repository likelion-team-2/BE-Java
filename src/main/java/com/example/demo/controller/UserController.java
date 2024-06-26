package com.example.demo.controller;

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

import java.util.UUID;

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
    public ResponseEntity<ResponseData<String>> signUp(@Valid @RequestBody UserRequestDTO userDTO) {
        System.out.println("Received user signup request: " + Json.pretty(userDTO));
        try {
            // Logic to create user
            String data = userService.signUp(userDTO);
            ResponseData<String> responseData = new ResponseData<>(HttpStatus.OK.value(), "User created", data);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            // Return error response
            ResponseError responseError = new ResponseError(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseError);
        }
    }
}
