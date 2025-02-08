package com.example.chatapp.Dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class UserDTO {

    private int userid;
    private String username;
    private String email;
    private String password;
}
