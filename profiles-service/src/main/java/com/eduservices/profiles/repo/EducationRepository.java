package com.eduservices.profiles.repo;

import com.eduservices.profiles.entity.EducationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<EducationDetails, Long> {
}
