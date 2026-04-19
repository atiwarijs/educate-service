package com.eduservices.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDTO {

	private String configKey;

	private String configValue;

	private String configValueType;

	private String orgCode;

	private String orgBranchCode;

}
