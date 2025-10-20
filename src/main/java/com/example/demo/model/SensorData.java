package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Enumerated(EnumType.STRING)
    private SensorType sensorType;

    private Double value;
    private LocalDateTime timestamp;
    private boolean anomaly;
}