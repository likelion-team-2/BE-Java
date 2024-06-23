package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserRequestSignInDTO;

public interface UserService {

    String signUp(UserRequestDTO requestDTO);

    /**
     * Service for handle user login
     * @param userRequestSignInDTO
     * @return  UserRequestDTO
     */
    UserRequestDTO signIn(UserRequestSignInDTO userRequestSignInDTO);

}
