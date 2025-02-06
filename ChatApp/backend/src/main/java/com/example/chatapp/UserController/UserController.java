package com.example.chatapp.UserController;

import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Service.UserService;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Entity.AppUser;

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
    public ResponseEntity<String> saveUser(@RequestBody UserDTO userDTO) {
        try {
            String result = userService.addUser(userDTO);
            if ("Email already exists".equals(result)) {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            } else if ("Username already exists".equals(result)) {
                return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("User " + result + " registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {

            return new ResponseEntity<>("An error occurred while registering the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDTO loginDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);


            AppUser user = userRepository.findByEmail(loginDTO.getEmail());

            if (user != null) {
                response.put("message", "Login Success");
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("userId", String.valueOf(user.getUserid()));

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "User not found");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            response.put("message", "Invalid credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
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

}
