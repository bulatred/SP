package com.example.demo.service;

import com.example.demo.model.Bus;
import com.example.demo.model.SensorData;
import com.example.demo.model.SensorType;
import com.example.demo.repository.BusRepository;
import com.example.demo.repository.SensorRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final BusRepository busRepository;

    public SensorService(SensorRepository sensorRepository, BusRepository busRepository) {
        this.sensorRepository = sensorRepository;
        this.busRepository = busRepository;
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

}
