// src/main/java/com/example/demo/repository/TokenRepository.java
package com.example.demo.repository;

import com.example.demo.model.security.Token;
import com.example.demo.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User user);
    Optional<Token> findByValue(String value);

    @Query("SELECT t FROM Token t WHERE t.user = :user AND t.revoked = false AND t.expired = false")
    List<Token> findAllValidTokensByUser(User user);
}