// src/main/java/com/example/demo/controller/SensorUploadController.java
package com.example.demo.controller;

import com.example.demo.model.SensorData;
import com.example.demo.service.SensorService;
import com.example.demo.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.repository.BusRepository;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorUploadController {

    private static final Logger log = LoggerFactory.getLogger(SensorUploadController.class);

    private final SensorService sensorService;
    private final BusRepository busRepository;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadSensors(@RequestParam("file") MultipartFile file) {
        log.info("Загрузка CSV-файла: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            log.warn("Загружен пустой файл");
            return ResponseEntity.badRequest().body("Файл пустой");
        }

        // ✅ Проверка MIME-типа
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("text/csv")) {
            log.warn("Неверный MIME-тип файла: {}", contentType);
            return ResponseEntity.badRequest().body("Разрешены только CSV-файлы (MIME: text/csv)");
        }

        // ✅ Проверка расширения
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
            log.warn("Неверное расширение файла: {}", originalFilename);
            return ResponseEntity.badRequest().body("Разрешены только .csv файлы");
        }

        try {
            String csvContent = new String(file.getBytes());
            List<SensorData> sensors = CsvUtil.parseSensorCsv(csvContent, busRepository);

            sensorService.saveAllSensors(sensors);

            log.info("Загружено {} датчиков из файла", sensors.size());
            return ResponseEntity.ok("Загружено " + sensors.size() + " датчиков");

        } catch (IOException e) {
            log.error("Ошибка при чтении файла", e);
            return ResponseEntity.status(500).body("Ошибка при чтении файла");
        } catch (Exception e) {
            log.error("Ошибка при обработке CSV", e);
            return ResponseEntity.status(500).body("Ошибка при обработке CSV: " + e.getMessage());
        }
    }
}