package com.eduservices.profiles.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduservices.profiles.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
}
