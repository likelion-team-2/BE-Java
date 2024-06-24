package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
