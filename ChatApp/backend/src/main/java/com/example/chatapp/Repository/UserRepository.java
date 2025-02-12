package com.example.chatapp.Repository;

import com.example.chatapp.Entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findOneByEmailAndPassword(String email, String password);

    AppUser findByEmail(String email);
    AppUser findByUsername(String username);
}
