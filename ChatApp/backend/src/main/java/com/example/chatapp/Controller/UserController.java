package com.example.chatapp.Controller;

import com.example.chatapp.Dto.LoginDTO;
import com.example.chatapp.Dto.UserDTO;
import com.example.chatapp.Service.UserService;
import com.example.chatapp.Repository.UserRepository;
import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Util.JwtUtil;
import com.example.chatapp.payload.response.UserResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            UserResult result = userService.addUser(userDTO);
            if (result instanceof UserResult.Success) {
                UserResult.Success s = (UserResult.Success) result;
                response.put("message", "User " + s.username() + " registered successfully");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else if (result instanceof UserResult.Failure) {
                UserResult.Failure f = (UserResult.Failure) result;
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
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDTO loginDTO,
                                                         HttpServletResponse response) {
        Map<String, String> resp = new HashMap<>();
        UserResult result = userService.loginUser(loginDTO);

        if (result instanceof UserResult.Success) {
            UserResult.Success success = (UserResult.Success) result;
            String jwt = jwtUtil.generateToken(success.username());
            ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            resp.put("message", "Login Success");
            resp.put("username", success.username());
            resp.put("userId", String.valueOf(success.userId()));
            return ResponseEntity.ok(resp);

        } else if (result instanceof UserResult.Failure) {
            UserResult.Failure failure = (UserResult.Failure) result;
            resp.put("message", failure.message());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        resp.put("message", "Unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logout successful");
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
