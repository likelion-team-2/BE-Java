package com.example.demo.service;

import com.example.demo.dto.request.ChatMessageRequestDto;
import com.example.demo.dto.response.ChatMessageResponseDto;
import com.example.demo.entities.Message;
import com.example.demo.entities.Session;

import java.util.List;

public interface MessageService {
    /**
     * Save chat message
     * @param chatMessage ChatMessageRequestDto
     * @return Message entity
     */
    Message saveChatMessage(ChatMessageRequestDto chatMessage);

    /**
     * Update chat message to redis
     * @param chatMessageResponse
     * @param session
     */
    void updateMessageToRedis(ChatMessageResponseDto chatMessageResponse, Session session);


}
