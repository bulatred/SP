// src/main/java/com/example/demo/controller/SensorController.java
package com.example.demo.controller;

import com.example.demo.model.SensorData;
import com.example.demo.model.SensorType;
import com.example.demo.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private static final Logger log = LoggerFactory.getLogger(SensorController.class);

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/{busId}")
    public ResponseEntity<SensorData> addSensor(
            @PathVariable Long busId,
            @RequestBody SensorData data) {
        log.info("Добавление сенсора для автобуса ID: {}", busId);
        try {
            SensorData created = sensorService.addSensorData(busId, data);
            log.info("Сенсор добавлен: ID={}, тип={}", created.getId(), created.getSensorType());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Ошибка при добавлении сенсора для автобуса ID: {}", busId, e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<SensorData>> getAllSensors() {
        log.info("Запрос всех сенсоров");
        List<SensorData> sensors = sensorService.getAllSensors();
        log.info("Возвращено {} сенсоров", sensors.size());
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{busId}")
    public ResponseEntity<List<SensorData>> getSensorsByBus(@PathVariable Long busId) {
        log.info("Запрос сенсоров для автобуса ID: {}", busId);
        List<SensorData> sensors = sensorService.getSensorsByBus(busId);
        log.info("Найдено {} сенсоров для автобуса ID: {}", sensors.size(), busId);
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<SensorData>> getAnomalies() {
        log.info("Запрос аномалий");
        List<SensorData> anomalies = sensorService.getAnomalies();
        log.info("Найдено {} аномалий", anomalies.size());
        return ResponseEntity.ok(anomalies);
    }

    @GetMapping("/anomalies/{busId}")
    public ResponseEntity<List<SensorData>> getAnomaliesByBus(@PathVariable Long busId) {
        log.info("Запрос аномалий для автобуса ID: {}", busId);
        List<SensorData> anomalies = sensorService.getAnomaliesByBus(busId);
        log.info("Найдено {} аномалий для автобуса ID: {}", anomalies.size(), busId);
        return ResponseEntity.ok(anomalies);
    }

    @GetMapping("/latest/{busId}")
    public ResponseEntity<Map<SensorType, SensorData>> getLatestSensors(@PathVariable Long busId) {
        log.info("Запрос последних сенсоров для автобуса ID: {}", busId);
        Map<SensorType, SensorData> latest = sensorService.getLatestByBus(busId);
        log.info("Получено {} типов сенсоров для автобуса ID: {}", latest.size(), busId);
        return ResponseEntity.ok(latest);
    }

    @GetMapping("/history/{busId}")
    public ResponseEntity<List<SensorData>> getHistory(@PathVariable Long busId) {
        log.info("Запрос истории сенсоров для автобуса ID: {}", busId);
        List<SensorData> history = sensorService.getHistoryByBus(busId);
        log.info("Найдено {} записей истории для автобуса ID: {}", history.size(), busId);
        return ResponseEntity.ok(history);
    }
}