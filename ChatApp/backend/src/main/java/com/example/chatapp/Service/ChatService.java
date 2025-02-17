package com.example.chatapp.Service;

import com.example.chatapp.Repository.ChatRepository;
import com.example.chatapp.Repository.MessageRepository;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Dto.ChatDTO;

import com.example.chatapp.Entity.ChatMessage;
import com.example.chatapp.Entity.Chat;
import com.example.chatapp.Entity.AppUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ChatDTO getOrCreateChat(Long user1Id, Long user2Id) {
        Optional<Chat> chat = chatRepository.findByUser1UseridAndUser2Userid(user1Id, user2Id);

        if (!chat.isPresent()) {
            chat = chatRepository.findByUser1UseridAndUser2Userid(user2Id, user1Id);
        }

        if (!chat.isPresent()) {
            chat = Optional.of(createChat(user1Id, user2Id));
        }

        Chat chatEntity = chat.get();
        return new ChatDTO(
                chatEntity.getId(),
                chatEntity.getUser1().getUserid(),
                chatEntity.getUser2().getUserid(),
                chatEntity.getUser1().getUsername(),
                chatEntity.getUser2().getUsername()
        );
    }


    @Transactional
    public Chat createChat(Long user1Id, Long user2Id) {
        AppUser user1 = userRepository.findById(user1Id).orElseThrow();
        AppUser user2 = userRepository.findById(user2Id).orElseThrow();

        Chat chat = new Chat(user1, user2);
        return chatRepository.save(chat);
    }
}
