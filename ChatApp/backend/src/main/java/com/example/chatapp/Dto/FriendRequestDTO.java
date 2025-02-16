package com.example.chatapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendRequestDTO {
    private Long senderUserid;
    private Long receiverUserid;
    private Long requesterUserId;
    private boolean status = false;
}
