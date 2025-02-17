package com.example.chatapp.UserController;

import com.example.chatapp.Dto.ChatMessageDTO;
import com.example.chatapp.Service.MessageService;
import com.example.chatapp.Entity.ChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long chatId) {
        List<ChatMessage> messages = messageService.getMessages(chatId);

        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(message -> new ChatMessageDTO(message))
                .collect(Collectors.toList());

        return ResponseEntity.ok(messageDTOs);
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(
            @RequestParam Long chatId,
            @RequestParam Long senderId,
            @RequestParam String content) {

        ChatMessage sentMessage = messageService.sendMessage(chatId, senderId, content);

        ChatMessageDTO messageDTO = new ChatMessageDTO(sentMessage);

        return ResponseEntity.ok(messageDTO);
    }
}
