package com.eduservices.profiles.repo;

import com.eduservices.profiles.entity.MedicalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRepository extends JpaRepository<MedicalDetails, Long> {
}
