package com.example.chatapp.Service;

import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Entity.Friendship;
import com.example.chatapp.Dto.FriendRequestInfoDTO;
import com.example.chatapp.payload.response.FriendshipResult;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class FriendshipService {

    @Autowired
    private UserRepository appUserRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    public FriendshipResult sendFriendshipRequest(AppUser sender, AppUser receiver) {

        if (sender == null || receiver == null) {
            return new FriendshipResult.Failure("User not found");
        }

        if (sender.getUserid().equals(receiver.getUserid())) {
            return new FriendshipResult.Failure("Cannot send a request to yourself");
        }

        List<Friendship> existingFriendships = friendshipRepository.findAllRequests(sender, receiver);

        if (!existingFriendships.isEmpty()) {
            Friendship friendship = existingFriendships.get(0);

            if (!friendship.isStatus()) {
                if (friendship.getRequestedBy().equals(sender)) {
                    return new FriendshipResult.Failure("You have already sent a request");
                }
                friendship.setStatus(true);
                friendshipRepository.save(friendship);
                return new FriendshipResult.Success("You have accepted the friend request!");
            } else {
                return new FriendshipResult.Failure("Friendship already accepted.");
            }
        }

        Friendship friendship = new Friendship(sender, receiver, sender, false);
        friendshipRepository.save(friendship);

        return new FriendshipResult.Success("Friendship request sent successfully.");
    }


    public List<AppUser> getFriends(AppUser user) {
        List<AppUser> friends = new ArrayList<>();

        List<Friendship> friendships = friendshipRepository.findAllAcceptedFriendshipsById(user);
        for (Friendship friendship : friendships) {
            if (friendship.getUser1() != user) {
                friends.add(friendship.getUser1());
            } else {
                friends.add(friendship.getUser2());
            }
        }
        return friends;
    }

    public List<FriendRequestInfoDTO> getPendingFriendRequestsForUser(Long userId) {
        return friendshipRepository.findPendingRequestsByUserId(userId);
    }



}
