// src/main/java/com/example/demo/service/AuthService.java
package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserLoggedDto;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void logout();
    UserLoggedDto getUserLoggedInfo();
}