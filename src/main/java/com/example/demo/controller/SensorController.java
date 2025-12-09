package com.example.demo.controller;

import com.example.demo.model.SensorData;
import com.example.demo.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.demo.model.SensorType;
import java.util.Map;


@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/{busId}")
    public ResponseEntity<SensorData> addSensor(
            @PathVariable Long busId,
            @RequestBody SensorData data) {
        SensorData created = sensorService.addSensorData(busId, data);
        return ResponseEntity.ok(created);
        }

    @GetMapping
    public ResponseEntity<List<SensorData>> getAllSensors() {
        return ResponseEntity.ok(sensorService.getAllSensors());
    }

    @GetMapping("/{busId}")
    public ResponseEntity<List<SensorData>> getSensorsByBus(@PathVariable Long busId) {
        return ResponseEntity.ok(sensorService.getSensorsByBus(busId));
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<SensorData>> getAnomalies() {
        List<SensorData> anomalies = sensorService.getAnomalies();
        return ResponseEntity.ok(anomalies);
    }

    @GetMapping("/anomalies/{busId}")
    public ResponseEntity<List<SensorData>> getAnomaliesByBus(@PathVariable Long busId) {
        List<SensorData> anomalies = sensorService.getAnomaliesByBus(busId);
        return ResponseEntity.ok(anomalies);
    }

    @GetMapping("/latest/{busId}")
    public ResponseEntity<Map<SensorType, SensorData>> getLatestSensors(@PathVariable Long busId) {
        return ResponseEntity.ok(sensorService.getLatestByBus(busId));
    }

    @GetMapping("/history/{busId}")
    public ResponseEntity<List<SensorData>> getHistory(@PathVariable Long busId) {
        return ResponseEntity.ok(sensorService.getHistoryByBus(busId));
    }


}
