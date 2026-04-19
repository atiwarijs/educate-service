package com.eduservices.profiles.service;

import com.eduservices.profiles.entity.MedicalDetails;
import com.eduservices.profiles.repo.MedicalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalService {

    private final MedicalRepository medicalRepository;

    public MedicalService(MedicalRepository medicalRepository) {
        this.medicalRepository = medicalRepository;
    }

    public List<MedicalDetails> findAll() {
        return medicalRepository.findAll();
    }

    public MedicalDetails findById(Long id) {
        return medicalRepository.findById(id).orElse(null);
    }

    public MedicalDetails save(MedicalDetails medical) {
        return medicalRepository.save(medical);
    }

    public MedicalDetails update(Long id, MedicalDetails medical) {
        Optional<MedicalDetails> existing = medicalRepository.findById(id);
        if (existing.isPresent()) {
            medical.setId(id);
            return medicalRepository.save(medical);
        }
        return null;
    }

    public void deleteById(Long id) {
        medicalRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return medicalRepository.existsById(id);
    }
}
