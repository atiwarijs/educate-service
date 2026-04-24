package com.eduservices.registrations.repo;

import com.eduservices.registrations.entity.RegistrationEntity;
import com.eduservices.repo.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends GenericRepository<RegistrationEntity, Long> {

    Optional<RegistrationEntity> findByRegNumber(String regNumber);

    List<RegistrationEntity> findByOrgId(Long orgId);

    Optional<RegistrationEntity> findByRegNumberAndOrgId(String regNumber, Long orgId);
}
