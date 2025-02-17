package com.example.chatapp.Entity;

import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Entity.ChatMessage;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false, referencedColumnName = "userid")
    private AppUser user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false, referencedColumnName = "userid")
    private AppUser user2;

    public Chat() {}

    public Chat(AppUser user1, AppUser user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
