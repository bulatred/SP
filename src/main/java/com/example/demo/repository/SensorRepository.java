package com.example.demo.repository;

import com.example.demo.model.SensorData;
import com.example.demo.model.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findByBusId(Long busId);
    
    List<SensorData> findTop1ByBusIdAndSensorTypeOrderByTimestampDesc(
            Long busId,
            SensorType sensorType
    );
}