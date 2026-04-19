package com.eduservices.profiles.repo;

import com.eduservices.profiles.entity.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalRepository extends JpaRepository<PersonalDetails, Long> {

    Optional<PersonalDetails> findByUserId(String userId);
}