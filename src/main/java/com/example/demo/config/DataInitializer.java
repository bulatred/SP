package com.example.demo.config;

import com.example.demo.model.Role;
import com.example.demo.model.Permission;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.PermissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roleRepo, PermissionRepository permRepo) {
        return args -> {
            Permission pRead = permRepo.findByTitle("SENSORS_READ").orElseGet(() -> permRepo.save(new Permission(null, "SENSORS_READ")));
            Permission pWrite = permRepo.findByTitle("SENSORS_WRITE").orElseGet(() -> permRepo.save(new Permission(null, "SENSORS_WRITE")));

            roleRepo.findByTitle("ROLE_USER").orElseGet(() -> {
                Role r = new Role();
                r.setTitle("ROLE_USER");
                r.setPermissions(Set.of(pRead));
                return roleRepo.save(r);
            });

            roleRepo.findByTitle("ROLE_ADMIN").orElseGet(() -> {
                Role r = new Role();
                r.setTitle("ROLE_ADMIN");
                r.setPermissions(Set.of(pRead, pWrite));
                return roleRepo.save(r);
            });
        };
    }
}
