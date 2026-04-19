package com.eduservices.profiles.repo;

import com.eduservices.profiles.entity.VehicleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleDetails, Long> {
}
