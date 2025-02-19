package com.example.chatapp.UserController;

import com.example.chatapp.Service.ChatService;
import com.example.chatapp.Service.MessageService;
import com.example.chatapp.Entity.Chat;
import com.example.chatapp.Dto.ChatDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "/{user1Id}/{user2Id}")
    public ResponseEntity<ChatDTO> getOrCreateChat(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        ChatDTO chatDTO = chatService.getOrCreateChat(user1Id, user2Id);
        return ResponseEntity.ok(chatDTO);
    }
}

