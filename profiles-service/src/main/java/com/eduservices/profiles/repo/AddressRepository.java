package com.eduservices.profiles.repo;

import com.eduservices.profiles.entity.AddressDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressDetails, Long> {
}
