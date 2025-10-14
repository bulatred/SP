package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.model.SensorData;
import com.example.demo.model.SensorType;

import org.springframework.stereotype.Service;

@Service
public class SensorService {
    private final List<SensorData> dataList = new ArrayList<>();

    public void addData(SensorData data) {
        if (data.getSensorType() == SensorType.ENGINE_TEMP && data.getValue() > 90) {
            data.setAnomaly(true);
        } else if (data.getSensorType() == SensorType.TIRE_PRESSURE && data.getValue() < 1.8) {
            data.setAnomaly(true);
        } else if (data.getSensorType() == SensorType.FUEL_LEVEL && data.getValue() < 10) {
            data.setAnomaly(true);
        } else {
            data.setAnomaly(false);
        }
        dataList.add(data);
        System.out.println("Получены данные от автобуса " + data.getBusId()
                + ": " + data.getSensorType() + " = " + data.getValue());
    }

    public List<SensorData> getAllData() {
        return dataList;
    }

    public List<SensorData> getAnomalies() {
        List<SensorData> anomalies = new ArrayList<>();
        for (SensorData data : dataList) {
            if (data.isAnomaly()) {
                anomalies.add(data);
            }
        }
        return anomalies;
    }

    public SensorData getLastByBusId(Long busId) {
        for (int i = dataList.size() - 1; i >= 0; i--) {
            if (dataList.get(i).getBusId().equals(busId)) {
                return dataList.get(i);
            }
        }
        return null;
    }
}
