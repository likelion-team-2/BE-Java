package com.example.demo.controller;

import com.example.demo.dto.request.ChatMessageRequestDto;
import com.example.demo.dto.response.ChatMessageResponseDto;
import com.example.demo.entities.Message;
import com.example.demo.service.impl.MessageServiceImpl;
import com.example.demo.service.impl.RedisServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

import java.util.Random;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageServiceImpl messageService;
    private final RedisServiceImpl redisService;

    @MessageMapping("/message")
    public void processMessage(@Payload ChatMessageRequestDto chatMessage) {
        Message message = messageService.saveChatMessage(chatMessage);

        ChatMessageResponseDto chatMessageResponse = ChatMessageResponseDto.builder()
                .id(ObjectUtils.isEmpty(message) ? String.valueOf(new Random().nextInt()) : message.getId().toString())
                .sender(chatMessage.getSender())
                .recipient(chatMessage.getRecipient())
                .content(chatMessage.getContent())
                .contentVi(chatMessage.getContent()) // TODO: Will be translated
                .contentKo(chatMessage.getContent()) // TODO: Will be translated
                .createdAt(ObjectUtils.isEmpty(message) ? null : message.getCreatedAt())
                .updatedAt(ObjectUtils.isEmpty(message) ? null : message.getUpdatedAt())
                .build();
        this.messagingTemplate.convertAndSend(
                "/topic/user/" + chatMessage.getRecipient(),
                chatMessageResponse
        );

        if (!ObjectUtils.isEmpty(message) && !ObjectUtils.isEmpty(message.getSession())) {
            messageService.updateMessageToRedis(chatMessageResponse, message.getSession());
        }
    }
}
