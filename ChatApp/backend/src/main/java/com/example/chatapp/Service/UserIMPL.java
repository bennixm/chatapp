package com.example.chatapp.Service;

import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Service.UserService;
import com.example.chatapp.payload.response.LoginMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserIMPL implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String addUser(UserDTO userDTO) {

        AppUser existingEmailUser = userRepository.findByEmail(userDTO.getEmail());
        AppUser existingUsernameUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUsernameUser != null && existingEmailUser != null) {
            return "We have found an existing user";
        }
        if (existingEmailUser != null) {
            return "Email already exists";
        }
        if (existingUsernameUser != null) {
            return "Username already exists";
        }

        AppUser user = new AppUser(
                userDTO.getUserid(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                this.passwordEncoder.encode(userDTO.getPassword())
        );

        userRepository.save(user);
        return user.getUsername();
    }

    @Override
    public LoginMessage loginUser(LoginDTO loginDTO) {

        AppUser user1 = userRepository.findByEmail(loginDTO.getEmail());
        if (user1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = user1.getPassword();

            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

                Authentication authentication = authenticationManager.authenticate(authenticationToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                return new LoginMessage("Login Success", true);
            } else {
                return new LoginMessage("Password Not Match", false);
            }
        } else {
            return new LoginMessage("Email not found", false);
        }
    }
}
