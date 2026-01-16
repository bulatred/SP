// src/main/java/com/example/demo/repository/SensorRepository.java
package com.example.demo.repository;

import com.example.demo.model.SensorData;
import com.example.demo.model.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findByBusId(Long busId);

    @Query("SELECT s FROM SensorData s WHERE s.bus.id = :busId AND s.sensorType = :sensorType ORDER BY s.timestamp DESC")
    List<SensorData> findByBusIdAndSensorTypeOrderByTimestampDesc(@Param("busId") Long busId, @Param("sensorType") SensorType sensorType);

    @Query("SELECT s FROM SensorData s WHERE s.bus.id = :busId AND s.sensorType = :sensorType ORDER BY s.timestamp DESC")
    List<SensorData> findTop1ByBusIdAndSensorTypeOrderByTimestampDesc(@Param("busId") Long busId, @Param("sensorType") SensorType sensorType);
}