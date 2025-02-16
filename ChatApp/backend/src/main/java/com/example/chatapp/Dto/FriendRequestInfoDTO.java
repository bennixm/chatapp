package com.example.chatapp.Dto;

import com.example.chatapp.Entity.AppUser;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FriendRequestInfoDTO {
    private AppUser user;
    private Long friendshipId;
}
