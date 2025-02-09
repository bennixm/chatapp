package com.example.chatapp.Repository;

import com.example.chatapp.Entity.FriendRequest;
import com.example.chatapp.Entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    FriendRequest findBySenderAndReceiver(AppUser sender, AppUser receiver);

    List<FriendRequest> findByReceiverAndAccepted(AppUser receiver, boolean accepted);

    List<FriendRequest> findBySenderAndAccepted(AppUser sender, boolean accepted);

}
