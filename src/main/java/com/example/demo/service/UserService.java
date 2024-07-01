package com.example.demo.service;

import com.example.demo.dto.request.ChangePasswordRequestDTO;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserRequestSignInDTO;
import com.example.demo.dto.response.ResponseGetUser;
import com.example.demo.dto.response.UserAuthResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserService {

    /**
     * Service for handle user sign up
     * @param requestDTO
     * @return  String
     */
    String signUp(UserRequestDTO requestDTO);

    /**
     * Service for handle user login
     * @param userRequestSignInDTO
     * @return  UserRequestDTO
     */
    UserAuthResponse signIn(UserRequestSignInDTO userRequestSignInDTO) throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * Service for handle user login
     * @param changePasswordRequestDTO
     */
    void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) throws NoSuchAlgorithmException, InvalidKeySpecException;


    /**
     * Service for handle user login
     * @param userRequesUserNameDTO
     * @return  User
     */
    ResponseGetUser getUser(String userRequesUserNameDTO) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
