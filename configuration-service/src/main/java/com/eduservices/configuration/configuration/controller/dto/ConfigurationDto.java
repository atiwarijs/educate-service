package com.eduservices.configuration.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
