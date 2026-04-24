package com.eduservices.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.eduservices.profile.keycloak", "com.eduservices.profile.client" })
public class SecurityServiceApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name", "SECURITY-SERVICE");
		SpringApplication.run(SecurityServiceApplication.class, args);
	}
}
