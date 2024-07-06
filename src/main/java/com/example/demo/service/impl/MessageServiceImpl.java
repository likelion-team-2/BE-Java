package com.example.demo.service.impl;

import com.example.demo.dto.request.ChatMessageRequestDto;
import com.example.demo.entities.Message;
import com.example.demo.entities.Session;
import com.example.demo.entities.User;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.SessionRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    /**
     * @{inheritDoc}
     */
    @Override
    public Message saveChatMessage(ChatMessageRequestDto chatMessage) {
        Optional<User> sender = userRepository.findByUsername(chatMessage.getSender());
        Optional<Session> session = sessionRepository.findById(chatMessage.getSessionId());
        if (sender.isEmpty() || session.isEmpty()) {
            return null;
        }
        return messageRepository.save(Message.builder()
                .user(sender.get())
                .session(session.get())
                .content(chatMessage.getContent())
                .contentKo(chatMessage.getContent()) // TODO: Will be translated
                .contentVi(chatMessage.getContent()) // TODO: Will be translated
                .createdAt(LocalDateTime.now())
                .build());

    }
}
