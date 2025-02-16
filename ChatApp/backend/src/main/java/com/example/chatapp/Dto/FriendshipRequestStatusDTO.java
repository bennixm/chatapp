package com.example.chatapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendshipRequestStatusDTO {
    private boolean status;

    public boolean getStatus() {
        return status;
    }
}
