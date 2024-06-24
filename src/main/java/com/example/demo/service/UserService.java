package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserRequestSignInDTO;

public interface UserService {

    /**
     * Service for handle user sign up
     * @param requestDTO
     * @return  Long
     */
    Long signUp(UserRequestDTO requestDTO);

    /**
     * Service for handle user login
     * @param userRequestSignInDTO
     * @return  UserRequestDTO
     */
    UserRequestDTO signIn(UserRequestSignInDTO userRequestSignInDTO);

}
