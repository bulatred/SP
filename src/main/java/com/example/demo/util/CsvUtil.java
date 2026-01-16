// src/main/java/com/example/demo/util/CsvUtil.java
package com.example.demo.util;

import com.example.demo.model.Bus;
import com.example.demo.model.SensorData;
import com.example.demo.model.SensorType;
import com.example.demo.repository.BusRepository;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CsvUtil {

    // src/main/java/com/example/demo/util/CsvUtil.java

    public static List<SensorData> parseSensorCsv(String csvContent, BusRepository busRepository) {
        List<SensorData> sensors = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new StringReader(csvContent))) {
            List<String[]> records = reader.readAll();
            records.remove(0); // Пропускаем заголовок

            for (String[] record : records) {
                if (record.length >= 4) {
                    SensorData sensor = new SensorData();
                    
                    // Находим автобус
                    Long busId = Long.parseLong(record[0]);
                    Bus bus = busRepository.findById(busId)
                        .orElseThrow(() -> new RuntimeException("Bus not found: " + busId));
                    
                    // ✅ Преобразуем в верхний регистр для enum
                    String sensorTypeStr = record[1].toUpperCase(); // "engine_temp" → "ENGINE_TEMP"
                    SensorType sensorType = SensorType.valueOf(sensorTypeStr);
                    
                    sensor.setBus(bus);
                    sensor.setSensorType(sensorType);  // ENGINE_TEMP
                    sensor.setValue(Double.parseDouble(record[2]));       // 95.5
                    sensor.setTimestamp(LocalDateTime.parse(record[3]));  // 2025-12-23T10:00:00
                    
                    // ✅ Рассчитываем аномалию
                    sensor.setAnomaly(checkAnomaly(sensor));

                    sensors.add(sensor);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при чтении CSV", e);
            throw new RuntimeException("Ошибка при чтении CSV", e);
        }
        return sensors;
    }

    // ✅ Метод проверки аномалии
    private static boolean checkAnomaly(SensorData data) {
        double value = data.getValue();
        return switch (data.getSensorType()) {
            case ENGINE_TEMP -> value > 100 || value < 60;  // температура > 100 или < 60
            case TIRE_PRESSURE -> value < 1.5 || value > 3.0;  // давление < 1.5 или > 3.0
            case FUEL_LEVEL -> value < 0 || value > 100;  // уровень < 0 или > 100
        };
    }
}