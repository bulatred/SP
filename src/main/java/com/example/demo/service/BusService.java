package com.example.demo.service;

import com.example.demo.model.Bus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusService {
    private final List<Bus> buses = new ArrayList<>();
    private Long nextId = 1L;

    public void addBus(Bus bus) {
        if (bus.getId() == null) {
            bus.setId(nextId++);
        }
        buses.add(bus);
    }
    
    public List<Bus> getAll() {
        return buses;
    }

    public Bus getById(Long id) {
        for (Bus bus : buses) {
            if (bus.getId().equals(id)) {
                return bus;
            }
        }
        return null;
    }

    public boolean deleteById(Long id) {
        return buses.removeIf(bus -> bus.getId().equals(id));
    }
}
