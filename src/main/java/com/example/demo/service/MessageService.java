package com.example.demo.service;

import com.example.demo.dto.request.ChatMessageRequestDto;
import com.example.demo.entities.Message;

public interface MessageService {
    /**
     * Save chat message
     * @param chatMessage ChatMessageRequestDto
     * @return Message entity
     */
    Message saveChatMessage(ChatMessageRequestDto chatMessage);
}
