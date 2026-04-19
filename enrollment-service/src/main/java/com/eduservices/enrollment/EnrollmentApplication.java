package com.eduservices.enrollment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class EnrollmentApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name", "ENROLLMENT-SERVICE");
		SpringApplication.run(EnrollmentApplication.class, args);
	}

}
