package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserLoggedDto;
import com.example.demo.service.AuthService;
import com.example.demo.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Попытка входа: {}", loginRequest.username());
        try {
            var response = authService.login(loginRequest);
            log.info("Успешный вход: {}", loginRequest.username());
            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(
                    response.token(), 24 * 60 * 60))
                .body(response);
        } catch (Exception e) {
            log.warn("Неудачная попытка входа: {}", loginRequest.username(), e);
            return ResponseEntity.status(401).body(new LoginResponse(false, null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout() {
        String username = null;
        try {
            username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        } catch (Exception ignored) {}
        log.info("Выход пользователя: {}", username != null ? username : "unknown");
        authService.logout();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie())
            .body(new LoginResponse(false, null));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    public ResponseEntity<UserLoggedDto> userLoggedInfo() {
        log.info("Запрос информации о пользователе");
        return ResponseEntity.ok(authService.getUserLoggedInfo());
    }
}