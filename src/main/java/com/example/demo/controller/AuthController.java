package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        String role = req.getRole() != null ? req.getRole() : "ROLE_USER";
        User created = userService.register(req.getUsername(), req.getPassword(), role);
        return ResponseEntity.ok("User created: " + created.getUsername());
    }

    // Вариант логина: используйте HTTP Basic (в Postman -> Authorization -> Basic)
    // Можно добавить endpoint /auth/me, который вернёт текущего username.
}
