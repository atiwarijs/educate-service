package com.eduservices.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class OrganizationApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name","organization-service");
		SpringApplication.run(OrganizationApplication.class, args);
	}

}
