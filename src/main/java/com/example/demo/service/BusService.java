package com.example.demo.service;

import com.example.demo.model.Bus;
import com.example.demo.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusService {

    private static final Logger log = LoggerFactory.getLogger(BusService.class);

    private final BusRepository busRepository;

    public Bus getById(Long id) {
        return busRepository.findById(id).orElse(null);
    }

    public Bus addBus(Bus bus) {
        return busRepository.save(bus);
    }

    public List<Bus> getAll() {
        return busRepository.findAll();
    }

    public void saveAllBuses(List<Bus> buses) {
        log.info("Сохранение {} автобусов в БД", buses.size());
        busRepository.saveAll(buses);
        log.info("Все автобусы сохранены");
    }

    public boolean deleteById(Long id) {
        if (busRepository.existsById(id)) {
            busRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Bus updateBus(Bus bus) {
        return busRepository.save(bus);
    }
}