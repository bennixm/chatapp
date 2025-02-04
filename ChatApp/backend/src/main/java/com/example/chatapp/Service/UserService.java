package com.example.chatapp.Service;

import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.payload.response.LoginMessage;

public interface UserService {
    String addUser(UserDTO userDTO);

    LoginMessage loginUser(LoginDTO loginDTO);

}