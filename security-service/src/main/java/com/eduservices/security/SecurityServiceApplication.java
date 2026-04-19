package com.eduservices.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SecurityServiceApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name", "SECURITY-SERVICE");
		SpringApplication.run(SecurityServiceApplication.class, args);
	}
}
