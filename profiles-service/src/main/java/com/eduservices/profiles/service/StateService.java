package com.eduservices.profiles.service;

import com.eduservices.profiles.entity.State;
import com.eduservices.profiles.repo.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateService {

    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    public State findById(Long id) {
        return stateRepository.findById(id).orElse(null);
    }

    public State save(State state) {
        return stateRepository.save(state);
    }

    public State update(Long id, State state) {
        Optional<State> existing = stateRepository.findById(id);
        if (existing.isPresent()) {
            state.setId(id);
            return stateRepository.save(state);
        }
        return null;
    }

    public void deleteById(Long id) {
        stateRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return stateRepository.existsById(id);
    }
}
