package com.example.demo.controller;

import com.example.demo.model.security.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        String hashedPassword = passwordEncoder.encode("password");
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "✅ Пароль для '" + username + "' обновлён. Новый хеш: " + hashedPassword;
    }
}