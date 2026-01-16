// src/main/java/com/example/demo/controller/FileUploadController.java
package com.example.demo.controller;

import com.example.demo.model.Bus;
import com.example.demo.service.BusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private final BusService busService;

    // ✅ ИСПОЛЬЗУЕМ АБСОЛЮТНЫЙ ПУТЬ
    private final String uploadDir = "C:/Users/Булат/KFU/demo/uploads/";

@PostMapping("/upload-photo")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> uploadPhoto(
        @RequestParam("file") MultipartFile file,
        @RequestParam Long busId) {
    
    log.info("Получен запрос на загрузку файла: {}", file.getOriginalFilename());
    log.info("Bus ID: {}", busId);

    if (file.isEmpty()) {
        log.warn("Файл пустой");
        return ResponseEntity.badRequest().body("Файл пустой");
    }

    // ✅ Проверка MIME-типа
    String contentType = file.getContentType();
    log.info("Content-Type файла: {}", contentType);
    
    if (contentType == null || 
        (!contentType.equals("image/jpeg") && 
         !contentType.equals("image/png") && 
         !contentType.equals("image/jpg"))) {
        log.warn("Неверный MIME-тип файла: {}", contentType);
        return ResponseEntity.badRequest().body("Разрешены только изображения (JPEG, PNG)");
    }

    // ✅ Проверка размера (максимум 5MB)
    if (file.getSize() > 5 * 1024 * 1024) {
        log.warn("Файл слишком большой: {} байт", file.getSize());
        return ResponseEntity.badRequest().body("Файл слишком большой (максимум 5MB)");
    }

    try {
        // ✅ Проверить, существует ли автобус
        Bus bus = busService.getById(busId);
        if (bus == null) {
            log.warn("Автобус не найден: {}", busId);
            return ResponseEntity.badRequest().body("Автобус не найден");
        }

        log.info("Папка для загрузки: {}", uploadDir);
        
        // ✅ Создать папку, если нет
        Path uploadPath = Paths.get(uploadDir);
        log.info("Путь к папке: {}", uploadPath.toAbsolutePath());
        
        if (!Files.exists(uploadPath)) {
            log.info("Создаю папку: {}", uploadPath.toAbsolutePath());
            Files.createDirectories(uploadPath);
        }

        // ✅ Создать уникальное имя файла
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String uniqueFilename = "bus_" + busId + "_" + UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(uniqueFilename);

        log.info("Сохраняю файл: {}", filePath.toAbsolutePath());

        // ✅ Сохранить файл
        file.transferTo(filePath.toFile());

        log.info("Файл успешно сохранён: {}", uniqueFilename);

        // ✅ Обновить photoUrl в автобусе
        bus.setPhotoUrl("/uploads/" + uniqueFilename);
        busService.updateBus(bus); // если есть такой метод

        return ResponseEntity.ok("Файл сохранён: " + uniqueFilename);

    } catch (IOException e) {
        log.error("Ошибка при сохранении файла", e);
        return ResponseEntity.status(500).body("Ошибка при сохранении файла: " + e.getMessage());
    } catch (Exception e) {
        log.error("Неизвестная ошибка", e);
        return ResponseEntity.status(500).body("Ошибка: " + e.getMessage());
    }
}

// src/main/java/com/example/demo/controller/FileUploadController.java

@DeleteMapping("/delete-photo/{busId}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> deletePhoto(@PathVariable Long busId) {
    log.info("Запрос на удаление фото для автобуса: {}", busId);

    Bus bus = busService.getById(busId);
    if (bus == null) {
        log.warn("Автобус не найден: {}", busId);
        return ResponseEntity.badRequest().body("Автобус не найден");
    }

    String photoUrl = bus.getPhotoUrl();
    if (photoUrl == null) {
        log.warn("Фото не найдено для автобуса: {}", busId);
        return ResponseEntity.badRequest().body("Фото не найдено");
    }

    try {
        // Удаляем файл
        String fileName = photoUrl.replace("/uploads/", "");
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("Файл удалён: {}", filePath.toAbsolutePath());
        }

        // Обновляем photoUrl
        bus.setPhotoUrl(null);
        busService.updateBus(bus);

        log.info("Фото удалено для автобуса: {}", busId);
        return ResponseEntity.ok("Фото удалено");

    } catch (IOException e) {
        log.error("Ошибка при удалении файла", e);
        return ResponseEntity.status(500).body("Ошибка при удалении файла: " + e.getMessage());
    }
}
}