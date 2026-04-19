package com.eduservices.profiles.repo;

import com.eduservices.profiles.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepository extends JpaRepository<FamilyMember, Long> {
}
