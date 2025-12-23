// src/main/java/com/example/demo/dto/UserLoggedDto.java
package com.example.demo.dto;
import java.util.List;
public record UserLoggedDto(String username, String role, List<String> permissions) {}