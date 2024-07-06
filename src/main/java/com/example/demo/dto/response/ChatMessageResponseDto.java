package com.example.demo.dto.response;

import com.example.demo.dto.request.ChatMessageRequestDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageResponseDto {

    private String sender;
    private String recipient;
    private String content;
    private String contentVi;
    private String contentKo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
