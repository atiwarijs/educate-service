package com.eduservices.profiles.service;

import com.eduservices.profiles.entity.ExperienceDetails;
import com.eduservices.profiles.repo.ExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public List<ExperienceDetails> findAll() {
        return experienceRepository.findAll();
    }

    public ExperienceDetails findById(Long id) {
        return experienceRepository.findById(id).orElse(null);
    }

    public ExperienceDetails save(ExperienceDetails experience) {
        return experienceRepository.save(experience);
    }

    public ExperienceDetails update(Long id, ExperienceDetails experience) {
        Optional<ExperienceDetails> existing = experienceRepository.findById(id);
        if (existing.isPresent()) {
            ExperienceDetails updated = existing.get();
            // Use reflection or ModelMapper to copy properties
            // For now, just save the incoming experience with the existing ID
            experience.setId(id);
            return experienceRepository.save(experience);
        }
        return null;
    }

    public void deleteById(Long id) {
        experienceRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return experienceRepository.existsById(id);
    }
}
