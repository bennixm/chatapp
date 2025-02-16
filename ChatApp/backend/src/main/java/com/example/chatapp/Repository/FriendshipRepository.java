package com.example.chatapp.Repository;

import com.example.chatapp.Entity.Friendship;
import com.example.chatapp.Entity.AppUser;

import com.example.chatapp.Dto.FriendRequestInfoDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE f.status = true AND ((f.user1 = :userId1 AND f.user2 = :userId2) OR (f.user1 = :userId2 AND f.user2 = :userId1))")
    public List<Friendship> findAllAcceptedFriendships(@Param("userId1") AppUser userId1, @Param("userId2") AppUser userId2);

    @Query("SELECT new com.example.chatapp.Dto.FriendRequestInfoDTO(f.user1, f.id) " +
            "FROM Friendship f " +
            "WHERE (f.user1.userid = :userId OR f.user2.userid = :userId) " +
            "AND f.status = false " +
            "AND f.requestedBy.userid != :userId")
    List<FriendRequestInfoDTO> findPendingRequestsByUserId(@Param("userId") Long userId);


    @Query("SELECT f FROM Friendship f WHERE ((f.user1 = :userId1 AND f.user2 = :userId2) OR (f.user1 = :userId2 AND f.user2 = :userId1))")
    public List<Friendship> findAllRequests(@Param("userId1") AppUser userId1, @Param("userId2") AppUser userId2);


    @Query("SELECT f FROM Friendship f WHERE f.user1 = :user OR f.user2 = :user")
    List<Friendship> findFriendshipsByUserId(@Param("user") AppUser user);

    @Query("SELECT f FROM Friendship f WHERE ((f.user1 = :user AND f.status = true) OR (f.user2 = :user AND f.status = true))")
    public List<Friendship> findAllAcceptedFriendshipsById(@Param("user") AppUser user);

}