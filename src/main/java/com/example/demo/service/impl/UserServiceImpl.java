package com.example.demo.service.impl;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserRequestSignInDTO;
import com.example.demo.entities.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.HashedPassword;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

import static com.example.demo.util.PasswordUtil.hashAndSaltPassword;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public Long signUp(UserRequestDTO userDto) {
        Optional<User> existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new ResourceNotFoundException("User đã tồn tại");
        } else if (userDto.getUsername().toLowerCase().contains("admin")){
            throw new ResourceNotFoundException("Can't create username with characters like admin");
        } else if (userDto.getPassword().length() < 8){
            throw new ResourceNotFoundException("Password must be at least 8 characters");
        } else if (userDto.getNickname().toLowerCase().contains("admin")){
            throw new ResourceNotFoundException("Can't create nickname with characters like admin");
        }

        String password = userDto.getPassword();
        HashedPassword hashedPassword = hashAndSaltPassword(password);
        User newUser = User.builder()
                .username(userDto.getUsername())
                .password(hashedPassword.getHashedPassword())
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .regionCountry(userDto.getRegion_country())
                .createdAt(new java.sql.Timestamp(System.currentTimeMillis()))
                .build();
        userRepository.save(newUser);

        return newUser.getId();
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
