package com.example.chatapp.controller;

import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Entity.FriendRequest;
import com.example.chatapp.payload.response.FriendshipResult;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @PostMapping("/send")
    public ResponseEntity<FriendshipResult> sendFriendRequest(@RequestParam int senderId, @RequestParam int receiverId) {
        Optional<AppUser> senderOpt = userRepository.findById(senderId);
        Optional<AppUser> receiverOpt = userRepository.findById(receiverId);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new FriendshipResult.Failure("Invalid sender or receiver."));
        }

        AppUser sender = senderOpt.get();
        AppUser receiver = receiverOpt.get();

        if (sender.getUserid() == receiver.getUserid()) {
            return ResponseEntity.badRequest().body(new FriendshipResult.Failure("You cannot send a friend request to yourself."));
        }

        if (sender.isFriend(receiver)) {
            return ResponseEntity.badRequest().body(new FriendshipResult.Failure("You are already friends."));
        }

        FriendRequest existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        if (existingRequest != null && !existingRequest.isAccepted()) {
            return ResponseEntity.badRequest().body(new FriendshipResult.Failure("Friend request already sent."));
        }

        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        friendRequestRepository.save(friendRequest);

        return ResponseEntity.ok(new FriendshipResult.Success("Friend request sent successfully."));
    }

    @PostMapping("/accept")
    public ResponseEntity<FriendshipResult> acceptFriendRequest(@RequestParam Long requestId) {
        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findById(requestId);

        if (friendRequestOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new FriendshipResult.Failure("Friend request not found."));
        }

        FriendRequest friendRequest = friendRequestOpt.get();
        if (friendRequest.isAccepted()) {
            return ResponseEntity.badRequest().body(new FriendshipResult.Failure("Friend request already accepted."));
        }

        friendRequest.setAccepted(true);
        friendRequestRepository.save(friendRequest);

        AppUser sender = friendRequest.getSender();
        AppUser receiver = friendRequest.getReceiver();
        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        userRepository.save(sender);
        userRepository.save(receiver);

        return ResponseEntity.ok(new FriendshipResult.Success("Friend request accepted successfully."));
    }

    @PostMapping("/reject")
    public ResponseEntity<FriendshipResult> rejectFriendRequest(@RequestParam Long requestId) {
        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findById(requestId);

        if (friendRequestOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new FriendshipResult.Failure("Friend request not found."));
        }

        friendRequestRepository.delete(friendRequestOpt.get());

        return ResponseEntity.ok(new FriendshipResult.Success("Friend request rejected successfully."));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<AppUser>> getFriends(@RequestParam int userId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        AppUser user = userOpt.get();
        return ResponseEntity.ok(List.copyOf(user.getFriends()));
    }

    @GetMapping("/received-requests")
    public ResponseEntity<List<FriendRequest>> getReceivedRequests(@RequestParam int userId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        AppUser user = userOpt.get();
        return ResponseEntity.ok(List.copyOf(user.getReceivedRequests()));
    }

    @GetMapping("/sent-requests")
    public ResponseEntity<List<FriendRequest>> getSentRequests(@RequestParam int userId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        AppUser user = userOpt.get();
        return ResponseEntity.ok(List.copyOf(user.getSentRequests()));
    }
}
