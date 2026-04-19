package com.eduservices.configuration.entity;


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
@Entity
@Table(name = "configuration_master", schema = "configurations")
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationEntity extends BaseEntity {

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{config.key}")
    @Column(name = "config_key", nullable = false)
    private String configKey;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{config.value}")
    @Column(name = "config_value", nullable = false)
    private String configValue;

    @Convert(converter = Encrypt.class)
    @NotEmpty(message = "{config.value.type}")
    @Column(name = "config_value_type", nullable = false)
    private String configValueType;

}
