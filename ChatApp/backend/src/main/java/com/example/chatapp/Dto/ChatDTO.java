package com.example.chatapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatDTO {
    private Long chatId;
    private Long user1Id;
    private Long user2Id;
    private String user1Username;
    private String user2Username;
}
