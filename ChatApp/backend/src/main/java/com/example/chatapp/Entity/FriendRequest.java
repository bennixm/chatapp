package com.example.chatapp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friend_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private AppUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private AppUser receiver;

    @Column(name = "accepted")
    private boolean accepted;

    @Column(name = "sent_time")
    private long sentTime;

    public FriendRequest(AppUser sender, AppUser receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.accepted = false;
        this.sentTime = System.currentTimeMillis();
    }
}
