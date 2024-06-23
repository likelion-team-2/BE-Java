package com.example.demo.service.impl;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserRequestSignInDTO;
import com.example.demo.entities.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String signUp(UserRequestDTO requestDTO) {
        System.out.println("Save user to database");
        if (requestDTO.getUsername().equals("admin")) {
             throw new ResourceNotFoundException("User Not Found");
        }
        return "User created";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserRequestDTO signIn(UserRequestSignInDTO userDto) {
        Optional<User> user = userRepository.findByUsername(userDto.getUsernameOrEmail());
        if (user.isEmpty()) {
            user = userRepository.findByEmail(userDto.getUsernameOrEmail());
            if (user.isEmpty()) {
                throw new ResourceNotFoundException("User Not Found");
            }

            User userFounded = user.get();
            UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                    .email(userFounded.getEmail())
                    .username(userFounded.getUsername())
                    .password(userFounded.getPassword())
                    .region_country(userFounded.getRegionCountry())
                    .nickname(userFounded.getNickname())
                    .build();

            if (passwordEncoder.matches(CharBuffer.wrap(userDto.getPassword()), userRequestDTO.getPassword())) {
                return userRequestDTO;
            }
        }
        throw new ResourceNotFoundException("Invalid password");
    }
}
