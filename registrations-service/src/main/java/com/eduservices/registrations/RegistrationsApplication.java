package com.eduservices.registrations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.eduservices.registrations","com.eduservices.common"})
@EnableDiscoveryClient
public class RegistrationsApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name","registration-service");
		SpringApplication.run(RegistrationsApplication.class, args);
	}

}
