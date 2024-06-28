package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.io.Serializable;

@Builder
public class UserRequestDTO implements Serializable {

    @NotBlank(message = "UserName must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    @Email(message = "Email invalid format")
    private String email;

    @NotBlank(message = "Nickname must not be blank")
    private String nickname;

    @NotNull(message = "Region Country not be null")
    private String region_country;

    public UserRequestDTO( String username, String password, String email, String nickname, String region_country) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.region_country = region_country;

    }

    public @NotBlank(message = "UserName must not be blank") String getUsername() {
        return username;
    }

    public @NotBlank(message = "Password must not be blank") String getPassword() {
        return password;
    }

    public @Email(message = "Email invalid format") String getEmail() {
        return email;
    }

    public @NotBlank(message = "Nickname must not be blank") String getNickname() {
        return nickname;
    }

    public @NotNull(message = "Region Country not be null") String getRegion_country() {
        return region_country;
    }

}
