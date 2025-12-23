// src/main/java/com/example/demo/dto/LoginResponse.java
package com.example.demo.dto;

public record LoginResponse(boolean isLogged, String roles, String token) {
    public LoginResponse(boolean isLogged, String roles) {
        this(isLogged, roles, null);
    }
}