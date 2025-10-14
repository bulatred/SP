package com.example.demo.controller;

import com.example.demo.model.SensorData;
import com.example.demo.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping
    public ResponseEntity<String> receiveData(@RequestBody SensorData data) {
        sensorService.addData(data);
        return ResponseEntity.ok("Данные получены");
    }

    @GetMapping
    public ResponseEntity<List<SensorData>> getAll() {
        return ResponseEntity.ok(sensorService.getAllData());
    }

    @GetMapping("/{busId}")
    public ResponseEntity<?> getLastForBus(@PathVariable Long busId) {
        SensorData data = sensorService.getLastByBusId(busId);
        if (data != null) return ResponseEntity.ok(data);
        else return ResponseEntity.notFound().build();
    }
}
