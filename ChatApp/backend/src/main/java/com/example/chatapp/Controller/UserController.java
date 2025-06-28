package com.example.chatapp.Controller;

import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Service.UserService;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.payload.response.UserResult;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            UserResult result = userService.addUser(userDTO);

            if (result instanceof UserResult.Success s) {
                response.put("message", "User " + s.username() + " registered successfully");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else if (result instanceof UserResult.Failure f) {
                response.put("message", f.message());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.put("message", "An error occurred while registering the user");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "Unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDTO loginDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            UserResult result = userService.loginUser(loginDTO);

            if (result instanceof UserResult.Success s) {
                response.put("message", "Login Success");
                response.put("username", s.username());
                response.put("userId", String.valueOf(s.userId()));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (result instanceof UserResult.Failure f) {
                response.put("message", f.message());
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            response.put("message", "Invalid credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        response.put("message", "Unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        try {

            request.getSession().invalidate();
            response.setStatus(HttpServletResponse.SC_OK);
            return new ResponseEntity<>("Logout successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during logout", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<AppUser> users = userRepository.findAll();
            List<UserDTO> userDTOs = users.stream()
                    .map(user -> new UserDTO(user.getUserid(), user.getUsername(), user.getEmail(), null))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
