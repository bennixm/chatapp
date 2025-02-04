package com.example.chatapp.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "userid", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userid;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(int userid, String username, String email, String password) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter and Setter for 'userid'
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    // Getter and Setter for 'username'
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for 'email'
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for 'password'
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
