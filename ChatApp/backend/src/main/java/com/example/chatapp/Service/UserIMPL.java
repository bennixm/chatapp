package com.example.chatapp.Service;

import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.Entity.User;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Service.UserService;

import com.example.chatapp.payload.response.LoginMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service

public class UserIMPL implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public String addUser(UserDTO userDTO) {

        User user = new User(

                userDTO.getUserid(),
                userDTO.getUsername(),
                userDTO.getEmail(),

                this.passwordEncoder.encode(userDTO.getPassword())
        );

        userRepository.save(user);

        return user.getUserename();
    }
    UserDTO userDTO;

    @Override
    public LoginMessage  loginUser(LoginDTO loginDTO) {
        String msg = "";
        User user1 = userRepository.findByEmail(loginDTO.getEmail());
        if (user1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = user1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<User> user = userRepository.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (user.isPresent()) {
                    return new LoginMessage("Login Success", true);
                } else {
                    return new LoginMessage("Login Failed", false);
                }
            } else {

                return new LoginMessage("password Not Match", false);
            }
        }else {
            return new LoginMessage("Email not exits", false);
        }


    }

}