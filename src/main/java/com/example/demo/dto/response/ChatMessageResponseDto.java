package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String sender;
    private String recipient;
    private String content;
    private String contentVi;
    private String contentKo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChatMessageResponseDto(String id, String sender, String recipient, String content, String contentVi, String contentKo) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.contentVi = contentVi;
        this.contentKo = contentKo;
    }
}