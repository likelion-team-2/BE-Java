package com.example.demo.dto.request;

import com.example.demo.util.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public class UserRequestSignInDTO implements Serializable {
    @Email(message = "Email should be valid")
    private String email;
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be numeric")
    @PhoneNumber(message = "phone invalid format")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    private String password;

    public UserRequestSignInDTO(String email, String phoneNumber, String password) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
