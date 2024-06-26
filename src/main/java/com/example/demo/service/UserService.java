package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserRequestSignInDTO;
import com.example.demo.dto.response.UserAuthResponse;
import com.example.demo.dto.response.UserSignInResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserService {

    /**
     * Service for handle user sign up
     * @param requestDTO
     * @return  Long
     */
    UserSignInResponse signUp(UserRequestDTO requestDTO);

    /**
     * Service for handle user login
     * @param userRequestSignInDTO
     * @return  UserRequestDTO
     */
    UserAuthResponse signIn(UserRequestSignInDTO userRequestSignInDTO) throws NoSuchAlgorithmException, InvalidKeySpecException;

}
