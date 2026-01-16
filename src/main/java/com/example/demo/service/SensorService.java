// src/main/java/com/example/demo/service/SensorService.java
package com.example.demo.service;

import com.example.demo.model.Bus;
import com.example.demo.model.SensorData;
import com.example.demo.model.SensorHistory;
import com.example.demo.model.SensorType;
import com.example.demo.repository.BusRepository;
import com.example.demo.repository.SensorHistoryRepository;
import com.example.demo.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final BusRepository busRepository;
    private final SensorHistoryRepository sensorHistoryRepository; // ✅ НОВОЕ
    private static final Logger log = LoggerFactory.getLogger(SensorService.class);

    public SensorService(SensorRepository sensorRepository, BusRepository busRepository, SensorHistoryRepository sensorHistoryRepository) {
        this.sensorRepository = sensorRepository;
        this.busRepository = busRepository;
        this.sensorHistoryRepository = sensorHistoryRepository; // ✅ НОВОЕ
    }

    public SensorData addSensorData(Long busId, SensorData data) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Автобус не найден"));
        data.setBus(bus);
        data.setTimestamp(LocalDateTime.now());
        data.setAnomaly(checkAnomaly(data));
        return sensorRepository.save(data);
    }

    public List<SensorData> getAllSensors() {
        return sensorRepository.findAll();
    }

    public List<SensorData> getSensorsByBus(Long busId) {
        return sensorRepository.findByBusId(busId);
    }

    private boolean checkAnomaly(SensorData data) {
        double value = data.getValue();
        return switch (data.getSensorType()) {
            case ENGINE_TEMP -> value > 100 || value < 60;
            case TIRE_PRESSURE -> value < 1.5 || value > 3.0;
            case FUEL_LEVEL -> value < 10 || value > 100;
        };
    }

    public List<SensorData> getAnomalies() {
        return sensorRepository.findAll()
                .stream()
                .filter(SensorData::isAnomaly)
                .toList();
    }

    public List<SensorData> getAnomaliesByBus(Long busId) {
        return sensorRepository.findByBusId(busId)
                .stream()
                .filter(SensorData::isAnomaly)
                .toList();
    }

    public List<SensorData> getHistoryByBus(Long busId) {
        return sensorRepository.findByBusId(busId);
    }

    public void saveAllSensors(List<SensorData> newSensors) {
        log.info("Сохранение {} датчиков в БД", newSensors.size());
        
        for (SensorData newSensor : newSensors) {
            // ✅ Найти существующий датчик по bus + sensorType
            List<SensorData> existingList = sensorRepository.findByBusIdAndSensorTypeOrderByTimestampDesc(
                newSensor.getBus().getId(), 
                newSensor.getSensorType()
            );
            
            if (!existingList.isEmpty()) {
                SensorData existing = existingList.get(0); // последний по времени
                
                // ✅ Сохранить старое значение в историю
                SensorHistory history = new SensorHistory();
                history.setSensorData(existing);
                history.setValue(existing.getValue());
                history.setTimestamp(existing.getTimestamp());
                sensorHistoryRepository.save(history);

                // ✅ Обновить текущее значение
                existing.setValue(newSensor.getValue());
                existing.setTimestamp(newSensor.getTimestamp());
                existing.setAnomaly(newSensor.isAnomaly());
                sensorRepository.save(existing);
            } else {
                // ✅ Новый датчик
                sensorRepository.save(newSensor);
            }
        }
        
        log.info("Все датчики обновлены");
    }

    public Map<SensorType, SensorData> getLatestByBus(Long busId) {
        Map<SensorType, SensorData> result = new EnumMap<>(SensorType.class);

        for (SensorType type : SensorType.values()) {
            sensorRepository
                .findTop1ByBusIdAndSensorTypeOrderByTimestampDesc(busId, type)
                .stream()
                .findFirst()
                .ifPresent(data -> result.put(type, data));
        }
        return result;
    }
}