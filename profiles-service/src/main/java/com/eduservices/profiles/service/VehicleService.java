package com.eduservices.profiles.service;

import com.eduservices.profiles.entity.VehicleDetails;
import com.eduservices.profiles.repo.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleDetails> findAll() {
        return vehicleRepository.findAll();
    }

    public VehicleDetails findById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public VehicleDetails save(VehicleDetails vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public VehicleDetails update(Long id, VehicleDetails vehicle) {
        Optional<VehicleDetails> existing = vehicleRepository.findById(id);
        if (existing.isPresent()) {
            vehicle.setId(id);
            return vehicleRepository.save(vehicle);
        }
        return null;
    }

    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return vehicleRepository.existsById(id);
    }
}
