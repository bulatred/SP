package com.example.demo.model;

import java.time.LocalDateTime;

public class SensorData {
    private Long id;
    private Long busId;
    private SensorType sensorType;
    private Double value;
    private LocalDateTime timestamp;
    private boolean anomaly;

    public SensorData() {}

    public SensorData(Long id, Long busId, SensorType sensorType, Double value, LocalDateTime timestamp, boolean anomaly) {
        this.id = id;
        this.busId = busId;
        this.sensorType = sensorType;
        this.value = value;
        this.timestamp = timestamp;
        this.anomaly = anomaly;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public SensorType getSensorType() { return sensorType; }
    public void setSensorType(SensorType sensorType) { this.sensorType = sensorType; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isAnomaly() { return anomaly; }
    public void setAnomaly(boolean anomaly) { this.anomaly = anomaly; }
}
