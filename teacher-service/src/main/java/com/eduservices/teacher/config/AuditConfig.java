package com.eduservices.teacher.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || !authentication.isAuthenticated()) {
				return Optional.of("system"); // fallback for system processes
			}

			if (authentication instanceof JwtAuthenticationToken jwtAuth) {
				// Keycloak puts username/email in claims
				String username = jwtAuth.getToken().getClaimAsString("preferred_username");
				if (username == null) {
					username = jwtAuth.getName(); // fallback
				}
				return Optional.of(username);
			}

			return Optional.of(authentication.getName()); // fallback
		};
	}
}
