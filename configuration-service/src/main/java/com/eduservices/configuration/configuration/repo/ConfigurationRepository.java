package com.eduservices.configuration.repo;

import com.eduservices.configuration.entity.ConfigurationEntity;
import com.eduservices.common.repo.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends GenericRepository<ConfigurationEntity, Long> {

    List<ConfigurationEntity> findByConfigKeyAndOrgId(String configKey , Long orgId);

    List<ConfigurationEntity> findByOrgId(Long orgId);

    List<ConfigurationEntity> findByConfigKey(String configKey);
}
