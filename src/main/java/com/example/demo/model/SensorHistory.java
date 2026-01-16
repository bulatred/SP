// src/main/java/com/example/demo/model/SensorHistory.java
package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "sensor_history")
@Data
@NoArgsConstructor   
@AllArgsConstructor
public class SensorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_data_id")
    private SensorData sensorData;

    private Double value;
    private LocalDateTime timestamp;
}