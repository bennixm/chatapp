package com.example.chatapp.Service;

import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Entity.Friendship;
import com.example.chatapp.Dto.FriendRequestInfoDTO;
import com.example.chatapp.Dto.FriendshipRequestStatusDTO;
import com.example.chatapp.payload.response.FriendshipResult;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    public FriendshipResult updateFriendRequestStatus(Long friendshipId, boolean status) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findById(friendshipId);

        if (friendshipOpt.isEmpty()) {
            return new FriendshipResult.Failure("Friendship request not found");
        }

        Friendship friendship = friendshipOpt.get();

        if (friendship.isStatus()) {
            return new FriendshipResult.Failure("Friendship request already accepted");
        }

        if (status) {
            friendship.setStatus(true);
            friendshipRepository.save(friendship);
            return new FriendshipResult.Success("Friend request accepted");
        } else {
            friendshipRepository.delete(friendship);
            return new FriendshipResult.Success("Friend request declined");
        }
    }

    public FriendshipResult acceptAllFriendRequests(Long userId) {
        List<FriendRequestInfoDTO> pendingFriendRequests = friendshipRepository.findPendingRequestsByUserId(userId);
        if (pendingFriendRequests.isEmpty()) {
            return new FriendshipResult.Failure("No pending friend requests found");
        }

        for (FriendRequestInfoDTO dto : pendingFriendRequests) {
            AppUser user = dto.getUser();
            Long friendshipId = dto.getFriendshipId();

            Optional<Friendship> friendshipOpt = friendshipRepository.findById(friendshipId);
            if (friendshipOpt.isPresent()) {
                Friendship friendship = friendshipOpt.get();
                friendship.setStatus(true);
                friendshipRepository.save(friendship);
            }
        }

        return new FriendshipResult.Success("All pending friend requests have been accepted");
    }

}
