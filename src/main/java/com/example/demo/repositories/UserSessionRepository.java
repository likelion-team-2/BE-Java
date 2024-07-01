package com.example.demo.repositories;

import com.example.demo.entities.User;
import com.example.demo.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long>{
    Optional<UserSession> findBySessionId(Integer sessionId);
    Optional<UserSession> findById(Long id);
    Optional<UserSession> findByUserId(User userId);

}
