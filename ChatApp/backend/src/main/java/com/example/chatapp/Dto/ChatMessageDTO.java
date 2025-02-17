package com.example.chatapp.Dto;

import com.example.chatapp.Entity.ChatMessage;
import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long id;
    private String senderUsername;
    private String content;
    private String timestamp;

    public ChatMessageDTO(ChatMessage message) {
        this.id = message.getId();
        this.senderUsername = message.getSender().getUsername();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp().toString();
    }
}
