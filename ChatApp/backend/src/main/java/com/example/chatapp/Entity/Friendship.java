package com.example.chatapp.Entity;

import com.example.chatapp.Entity.AppUser;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendship")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {

    @Id
    @Column(name = "friendship_id", length = 45)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id", referencedColumnName = "userid", nullable = false)
    private AppUser user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", referencedColumnName = "userid", nullable = false)
    private AppUser user2;

    @ManyToOne
    @JoinColumn(name = "requested_by", referencedColumnName = "userid", nullable = false)
    private AppUser requestedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false)
    private boolean status;

    public Friendship(AppUser user1, AppUser user2, AppUser requestedBy, boolean status) {
        this.user1 = user1;
        this.user2 = user2;
        this.requestedBy = requestedBy;
        this.status = status;
    }
}
