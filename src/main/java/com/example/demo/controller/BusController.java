package com.example.demo.controller;

import com.example.demo.model.Bus;
import com.example.demo.service.BusService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> addBus(@RequestBody Bus bus) {
        log.info("Добавлен автобус");
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
        log.info("Запрошен автобуc");
        Bus bus = busService.getById(id);
        if (bus != null) return ResponseEntity.ok(bus);
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBus(@PathVariable Long id) {
        log.info("Удалён автобус");
        if (busService.deleteById(id)) {
            return ResponseEntity.ok("Автобус удалён");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
