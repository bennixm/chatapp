package com.example.chatapp.Service;

import com.example.chatapp.Entity.AppUser;  // Entity User
import com.example.chatapp.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;  // Spring Security User
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user by email
        com.example.chatapp.Entity.AppUser appUser = userRepository.findByEmail(email);  // Use fully qualified name for Entity.User

        if (appUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Return UserDetails with authorities using Spring Security's User
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), new ArrayList<>());
    }
}
