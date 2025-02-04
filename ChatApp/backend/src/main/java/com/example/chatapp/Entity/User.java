package com.example.chatapp.Entity;


import jakarta.persistence.*;

@Entity
@Table(name="user")

public class User {

    @Id
    @Column(name="userid", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userid;

    @Column(name="username", length = 255)
    private String username;

    @Column(name="email", length = 255)
    private String email;

    @Column(name="password", length = 255)
    private String password;


    public User() {
    }

    public User(int userid, String username, String email, String password) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.password = password;
    }

}