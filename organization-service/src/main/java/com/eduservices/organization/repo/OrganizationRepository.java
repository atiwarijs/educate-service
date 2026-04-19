package com.eduservices.organization.repo;

import com.eduservices.organization.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {

    List<OrganizationEntity> findByStatus(String status);

    List<OrganizationEntity> findByOrgType(String type);

    OrganizationEntity findByOrgCode(String orgCode);

    OrganizationEntity findByOrgEmailId(String emailId);

    OrganizationEntity findByOrgPhoneNo(String phoneNo);

    List<OrganizationEntity> findByParentOrgId(Long parentOrgId);
}
