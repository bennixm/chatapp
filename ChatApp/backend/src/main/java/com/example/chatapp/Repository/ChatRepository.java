package com.example.chatapp.Repository;

import com.example.chatapp.Entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUser1UseridAndUser2Userid(Long user1Id, Long user2Id);
}
