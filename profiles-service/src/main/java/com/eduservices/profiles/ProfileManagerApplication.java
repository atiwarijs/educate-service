package com.eduservices.profiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class ProfileManagerApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name", "PROFILE-MANAGER");
		SpringApplication.run(ProfileManagerApplication.class, args);
	}

}
