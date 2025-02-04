package com.example.chatapp.UserController;

import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Service.UserService;
import com.example.chatapp.payload.response.LoginMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody UserDTO userDTO) {
        try {

            boolean isRegistered = userService.addUser(userDTO);
            if (isRegistered) {
                return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        try {

            LoginMessage loginMessage = userService.loginUser(loginDTO);


            if (loginMessage.getStatus()) {
                return new ResponseEntity<>(loginMessage.getMessage(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(loginMessage.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
