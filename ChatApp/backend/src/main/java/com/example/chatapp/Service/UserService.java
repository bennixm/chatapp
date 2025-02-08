package com.example.chatapp.Service;

import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.payload.response.LoginMessage;
import com.example.chatapp.payload.response.UserResult;

public interface UserService {
    UserResult addUser(UserDTO userDTO);

    UserResult loginUser(LoginDTO loginDTO);

}