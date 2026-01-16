// src/main/java/com/example/demo/model/security/Token.java
package com.example.demo.model.security;

import com.example.demo.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private String value;

    private LocalDateTime expiryDate;

    private boolean revoked;  
    private boolean expired;   

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}