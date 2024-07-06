package com.example.demo.repositories;

import com.example.demo.entities.Message;
import com.example.demo.entities.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySessionOrderByIdDesc(Session session, Pageable pageable);
}
