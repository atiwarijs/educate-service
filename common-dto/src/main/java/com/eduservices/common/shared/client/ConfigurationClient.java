package com.eduservices.shared.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eduservices.profile.dto.ConfigurationDTO;

@FeignClient(name = "configuration-service", url = "${configuration.api.url}")
public interface ConfigurationClient {

	@GetMapping("/api/configurations/{orgCode}/{branchCode}/{key}")
	ConfigurationDTO getConfig(@PathVariable("orgCode") String orgCode, @PathVariable("branchCode") String branchCode,
			@PathVariable("key") String key);
}
