package com.example.demo.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // "ROLE_USER" или "ROLE_ADMIN" — можно опционально
}
