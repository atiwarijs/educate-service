package com.eduservices.configuration.controller.dto;


import com.eduservices.configuration.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.eduservices.common.service.Encrypt;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDto {

    private Long id;
    private Long orgId;
    private String configKey;
    private String configValue;
    private String configValueType;

}
