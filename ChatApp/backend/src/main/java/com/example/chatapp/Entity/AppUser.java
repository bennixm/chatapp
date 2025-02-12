package com.example.chatapp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @Column(name = "userid", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userid;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "password", length = 255)
    private String password;


    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        AppUser appUser = (AppUser) object;
        return userid == appUser.userid;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userid != null ? userid.hashCode() : 0);
        return result;
    }

}
