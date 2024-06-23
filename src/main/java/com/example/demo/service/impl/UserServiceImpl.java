package com.example.demo.service.impl;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Override
    public String signUp(UserRequestDTO requestDTO) {
        System.out.println("Save user to database");
        if (requestDTO.getName().equals("admin")) {
             throw new ResourceNotFoundException("User Not Found");
        }
        return "User created";
    }
}
