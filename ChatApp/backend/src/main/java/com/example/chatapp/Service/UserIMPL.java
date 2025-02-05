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
    private AuthenticationManager authenticationManager;  // AuthenticationManager to authenticate the user

    @Override
    public String addUser(UserDTO userDTO) {
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
        // Check if the user exists based on email
        AppUser user1 = userRepository.findByEmail(loginDTO.getEmail());
        if (user1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = user1.getPassword();

            // Validate if the provided password matches the stored password
            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                // Use AuthenticationManager to authenticate the user
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

                // Authenticate the token
                Authentication authentication = authenticationManager.authenticate(authenticationToken);

                // Set the authentication in SecurityContextHolder to create the session
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Return success message with status
                return new LoginMessage("Login Success", true);
            } else {
                return new LoginMessage("Password Not Match", false);
            }
        } else {
            return new LoginMessage("Email not found", false);
        }
    }
}
