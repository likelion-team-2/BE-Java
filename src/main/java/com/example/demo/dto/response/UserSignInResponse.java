package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSignInResponse {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String region_country;
}
