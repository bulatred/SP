// src/main/java/com/example/demo/controller/BusController.java
package com.example.demo.controller;

import com.example.demo.model.Bus;
import com.example.demo.service.BusService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addBus(@Valid @RequestBody Bus bus) {  // ← @Valid
        log.info("Добавлен автобус: {}", bus.getModel());
        busService.addBus(bus);
        return ResponseEntity.ok("Автобус добавлен");
    }

    @GetMapping
    public ResponseEntity<List<Bus>> getAll() {
        log.info("Запрошены автобусы");
        return ResponseEntity.ok(busService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long id) {
        log.info("Запрошен автобус ID: {}", id);
        Bus bus = busService.getById(id);
        if (bus != null) return ResponseEntity.ok(bus);
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBus(@PathVariable Long id) {
        log.info("Удалён автобус ID: {}", id);
        if (busService.deleteById(id)) {
            return ResponseEntity.ok("Автобус удалён");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}