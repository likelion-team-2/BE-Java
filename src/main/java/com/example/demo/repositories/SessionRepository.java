package com.example.demo.repositories;

import com.example.demo.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findById(Long id);
    List<Session> findAll();
}
