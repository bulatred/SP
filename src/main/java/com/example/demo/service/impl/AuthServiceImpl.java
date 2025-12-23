// src/main/java/com/example/demo/service/impl/AuthServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserLoggedDto;
import com.example.demo.model.security.Token;
import com.example.demo.model.security.User;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Аутентификация пользователя: {}", request.username());
        try {
            // 1. Аутентифицируем
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
                )
            );

            // 2. Получаем пользователя
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String username = userDetails.getUsername();
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден после аутентификации: {}", username);
                    return new RuntimeException("User not found after auth");
                });

            // 3. Генерируем токен
            String token = jwtService.generateToken(userDetails);
            log.info("Токен сгенерирован для пользователя: {}", username);

            // 4. Отзыв старых токенов
            revokeAllUserTokens(user);
            log.info("Отозваны старые токены для пользователя: {}", username);

            // 5. Сохраняем новый токен
            Token newToken = new Token();
            newToken.setUser(user);
            newToken.setValue(token);
            newToken.setRevoked(false);
            newToken.setExpired(false);
            tokenRepository.save(newToken);
            log.info("Новый токен сохранён для пользователя: {}", username);

            // 6. Устанавливаем аутентификацию
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("Аутентификация успешна для пользователя: {}", username);

            return new LoginResponse(true, "ROLE_USER", token);
        } catch (Exception e) {
            log.warn("Ошибка аутентификации для пользователя {}: {}", request.username(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            log.info("Выход пользователя: {}", username);
            userRepository.findByUsername(username).ifPresent(this::revokeAllUserTokens);
            SecurityContextHolder.clearContext();
            log.info("Пользователь вышел: {}", username);
        } else {
            log.debug("Попытка выхода неавторизованным пользователем");
        }
    }

    @Override
    public UserLoggedDto getUserLoggedInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            log.warn("Попытка получения информации о пользователе без аутентификации");
            throw new RuntimeException("Not authenticated");
        }
        String username = auth.getName();
        log.info("Запрос информации о пользователе: {}", username);
        return new UserLoggedDto(username, "ROLE_USER", List.of("READ", "WRITE"));
    }

    private void revokeAllUserTokens(User user) {
        List<Token> tokens = tokenRepository.findByUser(user);
        log.info("Отзыв {} токенов для пользователя: {}", tokens.size(), user.getUsername());
        tokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(tokens);
        log.info("Токены отозваны для пользователя: {}", user.getUsername());
    }
}