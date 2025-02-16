package com.example.chatapp.UserController;

import com.example.chatapp.Dto.FriendRequestDTO;
import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Entity.Friendship;

import com.example.chatapp.Service.FriendshipService;
import com.example.chatapp.Dto.FriendRequestInfoDTO;

import com.example.chatapp.Repository.FriendshipRepository;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.payload.response.FriendshipResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/send")
    public ResponseEntity<FriendshipResult> sendFriendshipRequest(@RequestBody FriendRequestDTO friendRequestDTO) {

        AppUser sender = userRepository.findById(friendRequestDTO.getSenderUserid()).orElse(null);
        AppUser receiver = userRepository.findById(friendRequestDTO.getReceiverUserid()).orElse(null);

        if (sender == null || receiver == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FriendshipResult.Failure("User not found"));
        }

        FriendshipResult result = friendshipService.sendFriendshipRequest(sender, receiver);

        return result instanceof FriendshipResult.Success success
                ? ResponseEntity.ok(success)
                : ResponseEntity.badRequest().body((FriendshipResult.Failure) result);
    }

    @GetMapping("/getfriends")
    public ResponseEntity<List<AppUser>> getFriends(@RequestParam Long userId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);

        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        AppUser user = userOpt.get();

        List<AppUser> friends = friendshipService.getFriends(user);

        return ResponseEntity.ok(friends);
    }

    @GetMapping("/getFriendRequests")
    public ResponseEntity<List<FriendRequestInfoDTO>> getPendingFriendRequests(@RequestParam Long userId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<FriendRequestInfoDTO> pendingRequests = friendshipService.getPendingFriendRequestsForUser(userId);

        if (pendingRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pendingRequests);
        }

        return ResponseEntity.ok(pendingRequests);
    }


}

