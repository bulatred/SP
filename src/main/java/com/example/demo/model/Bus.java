package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Bus {
    private Long id;                
    private String model;           
    private List<SensorData> sensors; 

    public Bus() {
        this.sensors = new ArrayList<>();
    }

    public Bus(Long id, String model) {
        this.id = id;
        this.model = model;
        this.sensors = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public List<SensorData> getSensors() { return sensors; }
    public void setSensors(List<SensorData> sensors) { this.sensors = sensors; }

    public void addSensorData(SensorData data) {
        sensors.add(data);
    }
}

    