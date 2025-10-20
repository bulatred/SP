package com.example.demo.model;
import jakarta.persistence.*;
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
    private String model;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SensorData> sensors = new ArrayList<>();

    public void addSensorData(SensorData data) {
        sensors.add(data);
        data.setBus(this);
    }
}

    