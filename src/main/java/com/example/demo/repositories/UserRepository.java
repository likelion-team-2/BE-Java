package com.example.demo.repositories;

import  com.example.demo.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    List<User> findByNicknameContaining(String nickname);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
