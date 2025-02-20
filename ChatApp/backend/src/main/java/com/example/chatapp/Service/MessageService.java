package com.example.chatapp.Service;

import com.example.chatapp.Repository.ChatRepository;
import com.example.chatapp.Repository.MessageRepository;
import com.example.chatapp.Repository.UserRepository;


import com.example.chatapp.Entity.ChatMessage;
import com.example.chatapp.Entity.Chat;
import com.example.chatapp.Entity.AppUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ChatMessage sendMessage(Long chatId, Long senderId, String content) {
        if (content.length() > 255) {
            throw new IllegalArgumentException("Message cannot exceed 255 characters.");
        }

        Chat chat = chatRepository.findById(chatId).orElseThrow();
        AppUser sender = userRepository.findById(senderId).orElseThrow();

        ChatMessage message = new ChatMessage();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);

        return messageRepository.save(message);
    }

    public List<ChatMessage> getMessages(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }
}
