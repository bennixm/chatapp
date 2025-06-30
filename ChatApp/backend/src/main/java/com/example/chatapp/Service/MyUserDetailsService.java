package com.example.chatapp.Service;

import com.example.chatapp.Entity.AppUser;
import com.example.chatapp.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
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

        com.example.chatapp.Entity.AppUser appUser = userRepository.findByEmail(email);

        if (appUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }


        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), new ArrayList<>());
    }

    public UserDetails loadUserByUsernameFromToken(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username);

        if (appUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new User(appUser.getUsername(), appUser.getPassword(), new ArrayList<>());
    }
}
