package com.example.chatapp.Service;

import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Entity.FriendRequest;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Service.UserService;
import com.example.chatapp.payload.response.LoginMessage;
import com.example.chatapp.payload.response.UserResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.HashSet;


@Service
public class UserIMPL implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserResult addUser(UserDTO userDTO) {

        AppUser existingEmailUser = userRepository.findByEmail(userDTO.getEmail());
        AppUser existingUsernameUser = userRepository.findByUsername(userDTO.getUsername());

        if (existingUsernameUser != null && existingEmailUser != null) {
            return new UserResult.Failure("We have found an existing user");
        }
        if (existingEmailUser != null) {
            return new UserResult.Failure("Email already exists");
        }
        if (existingUsernameUser != null) {
            return new UserResult.Failure("Username already exists");
        }

        AppUser user = new AppUser(
                userDTO.getUserid(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                this.passwordEncoder.encode(userDTO.getPassword()),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );

        userRepository.save(user);
        return new UserResult.Success(user.getUsername(), user.getUserid());
    }

    @Override
    public UserResult loginUser(LoginDTO loginDTO) {
        AppUser user1 = userRepository.findByEmail(loginDTO.getEmail());
        if (user1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = user1.getPassword();

            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                return new UserResult.Success(user1.getUsername(), user1.getUserid());
            } else {
                return new UserResult.Failure("Password Not Match");
            }
        } else {
            return new UserResult.Failure("Email not found");
        }
    }

}
