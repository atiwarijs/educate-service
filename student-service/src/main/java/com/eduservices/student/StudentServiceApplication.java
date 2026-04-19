package com.eduservices.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class StudentServiceApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name", "STUDENT-SERVICE");
		SpringApplication.run(StudentServiceApplication.class, args);
	}

}
