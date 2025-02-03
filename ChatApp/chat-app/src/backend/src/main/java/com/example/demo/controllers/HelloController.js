package com.example.demo.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Allow Vue.js to access
public class HelloController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Spring Boot!";
    }
}
