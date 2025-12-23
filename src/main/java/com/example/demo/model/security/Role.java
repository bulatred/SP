// src/main/java/com/example/demo/model/security/Role.java
package com.example.demo.model.security;

import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    private String name = "ROLE_USER";
    private Set<String> permissions = Set.of("READ", "WRITE");

    @Override
    public String getAuthority() {
        return name;
    }
}