// src/main/java/com/example/demo/model/Bus.java
package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor   
@AllArgsConstructor
@Entity
@Table(name = "buses")
public class Bus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                

    @NotBlank(message = "Модель автобуса обязательна")
    @Size(min = 2, max = 100, message = "Модель автобуса должна быть от 2 до 100 символов")
    private String model;

    private String photoUrl;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SensorData> sensors = new ArrayList<>();

    public void addSensorData(SensorData data) {
        sensors.add(data);
        data.setBus(this);
    }
}