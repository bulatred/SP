// src/main/java/com/example/demo/DemoApplication.java

package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.security.User;
import com.example.demo.model.security.ERole;
import com.example.demo.repository.UserRepository;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // ✅ Добавляем бин для создания пользователей
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Создаем админа
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole(ERole.ROLE_ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
            }

            // Создаем обычного юзера
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRole(ERole.ROLE_USER);
                user.setEnabled(true);
                userRepository.save(user);
            }
        };
    }
}