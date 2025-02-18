package com.example.chatapp.UserController;

import com.example.chatapp.Dto.ChatMessageDTO;
import com.example.chatapp.Service.MessageService;
import com.example.chatapp.Entity.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long chatId) {
        List<ChatMessage> messages = messageService.getMessages(chatId);

        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(ChatMessageDTO::new)
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

        messagingTemplate.convertAndSend("/topic/messages/" + chatId, messageDTO);

        return ResponseEntity.ok(messageDTO);
    }
}
