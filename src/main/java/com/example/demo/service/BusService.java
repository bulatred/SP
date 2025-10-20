package com.example.demo.service;

import com.example.demo.model.Bus;
import org.springframework.stereotype.Service;
import com.example.demo.repository.BusRepository;
import java.util.List;

@Service
public class BusService {
    private final BusRepository BusRepository;

    public BusService(BusRepository BusRepository) {
        this.BusRepository = BusRepository;
    }

    public List<Bus> getAll() {
        return BusRepository.findAll();
    }

    public Bus getById(Long id) {
        return BusRepository.findById(id).orElse(null);
    }

    public Bus addBus(Bus bus) {
        return BusRepository.save(bus);
    }

    public boolean deleteById(Long id) {
    if (BusRepository.existsById(id)) {
        BusRepository.deleteById(id);
        return true;
    } else {
        return false;
    }
}

}
