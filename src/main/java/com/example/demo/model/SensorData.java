// src/main/java/com/example/demo/model/SensorData.java
package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor   
@AllArgsConstructor
@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    @JsonBackReference
    private Bus bus;

    @NotNull(message = "Тип датчика обязателен")
    @Enumerated(EnumType.STRING)
    private SensorType sensorType;

    @NotNull(message = "Значение не может быть пустым")
    @Min(value = -100, message = "Значение не может быть меньше -100")
    @Max(value = 1000, message = "Значение не может быть больше 1000")
    private Double value;

    private LocalDateTime timestamp;
    private boolean anomaly;
}