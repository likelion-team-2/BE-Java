package com.example.demo.service.impl;

import com.example.demo.dto.request.ChatMessageRequestDto;
import com.example.demo.dto.response.ChatMessageResponseDto;
import com.example.demo.entities.Message;
import com.example.demo.entities.Session;
import com.example.demo.entities.User;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.SessionRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.MessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final RedisServiceImpl redisService;
    private final GeminiServiceImpl geminiService;

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

        String apiResponse = geminiService.callApi(chatMessage.getContent());
        JsonNode nestedNode = this.parseMessageFromJson(apiResponse);
        String contentVi = nestedNode.path("contentVi").asText();
        String contentKo = nestedNode.path("contentKo").asText();

        return messageRepository.save(Message.builder()
                .user(sender.get())
                .session(session.get())
                .content(chatMessage.getContent())
                .contentKo(contentKo)
                .contentVi(contentVi)
                .createdAt(LocalDateTime.now())
                .build());

    }

    @Override
    public void updateMessageToRedis(ChatMessageResponseDto chatMessageResponse, Session session) {
        Message lastMessage = this.getLastElementOfLastFifteenMessage(session);

        if (!ObjectUtils.isEmpty(lastMessage)) {
            redisService.delete(lastMessage.getId().toString(), session.getId().toString());
        }
        redisService.saveMessage(chatMessageResponse, session.getId().toString());
    }

    private Message getLastElementOfLastFifteenMessage(Session session) {
        Pageable pageable = PageRequest.of(0, 15);
        List<Message> lastFifteenMessages = messageRepository.findBySessionOrderByIdDesc(session, pageable);
        if (lastFifteenMessages.isEmpty()) {
            return null;
        }
        return lastFifteenMessages.get(lastFifteenMessages.size() - 1);
    }

    /**
     * Parse message from response of gemini api response
     * @param json JSON string
     * @return parsed message
     */
    private JsonNode parseMessageFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Parse the outer JSON to get the nested JSON string
            JsonNode rootNode = mapper.readTree(json);
            String nestedJson = rootNode.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            return mapper.readTree(nestedJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
