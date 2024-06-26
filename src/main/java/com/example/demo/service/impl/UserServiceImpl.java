package com.example.demo.service.impl;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserRequestSignInDTO;
import com.example.demo.dto.response.UserAuthResponse;
import com.example.demo.entities.RefreshToken;
import com.example.demo.entities.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.HashedPassword;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import com.example.demo.util.UUID;

import com.example.demo.util.PasswordUtil;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenServiceImpl refreshTokenService;

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public UUID signUp(UserRequestDTO userDto) {
        Optional<User> existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            //UserName already exist
            throw new ResourceNotFoundException("1");
        } else if (userDto.getUsername().toLowerCase().contains("admin")){
            //Can't create username with characters like admin
            throw new ResourceNotFoundException("2");
        } else if (userDto.getPassword().length() < 8){
            //Password must be at least 8 characters
            throw new ResourceNotFoundException("3");
        } else if (userDto.getNickname().toLowerCase().contains("admin")){
            //Can't create nickname with characters like admin
            throw new ResourceNotFoundException("4");
        } else if (userRepository.findByEmail(userDto.getEmail()).isPresent()){
           //Email already exist
            throw new ResourceNotFoundException("5");
        }

        String password = userDto.getPassword();
        HashedPassword hashedPassword = PasswordUtil.hashAndSaltPassword(password);
        String user_id = UUID.GenerateUUID();
        User newUser = User.builder()
                .userId(user_id)
                .username(userDto.getUsername())
                .password(hashedPassword.getHashedPassword())
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .regionCountry(userDto.getRegion_country())
                .createdAt(new java.sql.Timestamp(System.currentTimeMillis()))
                .build();
        userRepository.save(newUser);
        return "true";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAuthResponse signIn(UserRequestSignInDTO userRequestDto) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String usernameOrEmail = userRequestDto.getUsernameOrEmail();
        Optional<User> user = userRepository.findByUsername(usernameOrEmail);
        if (user.isEmpty()) {
            user = userRepository.findByEmail(usernameOrEmail);
            if (user.isEmpty()) {
                throw new ResourceNotFoundException("User Not Found");
            }
            User userFounded = user.get();

            if (PasswordUtil.verifyPassword(userRequestDto.getPassword(), userFounded.getPassword(), "salt")) { //TODO: Get salt value
                String accessToken = jwtUtil.generateAccessTokenFromUsername(userFounded.getUsername());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userFounded.getId());
                return new UserAuthResponse(accessToken, refreshToken.getToken(), userFounded.getId(),
                        userFounded.getUsername(), userFounded.getEmail(), userFounded.getNickname(),
                        userFounded.getRegionCountry());
            }
            throw new ResourceNotFoundException("Invalid password");
        }
        throw new ResourceNotFoundException("User Not Found");
    }
}
