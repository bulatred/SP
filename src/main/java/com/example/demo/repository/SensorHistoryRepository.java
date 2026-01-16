// src/main/java/com/example/demo/repository/SensorHistoryRepository.java
package com.example.demo.repository;

import com.example.demo.model.SensorHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorHistoryRepository extends JpaRepository<SensorHistory, Long> {
}