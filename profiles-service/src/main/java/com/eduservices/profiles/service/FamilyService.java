package com.eduservices.profiles.service;

import com.eduservices.profiles.entity.FamilyMember;
import com.eduservices.profiles.repo.FamilyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final ModelMapper modelMapper;

    public FamilyService(FamilyRepository familyRepository, ModelMapper modelMapper) {
        this.familyRepository = familyRepository;
        this.modelMapper = modelMapper;
    }

    public List<FamilyMember> findAll() {
        return familyRepository.findAll();
    }

    public FamilyMember findById(Long id) {
        return familyRepository.findById(id).orElse(null);
    }

    public FamilyMember save(FamilyMember family) {
        return familyRepository.save(family);
    }

    public FamilyMember update(Long id, FamilyMember family) {
        Optional<FamilyMember> existing = familyRepository.findById(id);
        if (existing.isPresent()) {
            FamilyMember updated = existing.get();
            modelMapper.map(family, updated);
            return familyRepository.save(updated);
        }
        return null;
    }

    public void deleteById(Long id) {
        familyRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return familyRepository.existsById(id);
    }
}
