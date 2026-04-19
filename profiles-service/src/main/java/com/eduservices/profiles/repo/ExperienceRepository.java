package com.eduservices.profiles.repo;

import com.eduservices.profiles.entity.ExperienceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends JpaRepository<ExperienceDetails, Long> {
}
