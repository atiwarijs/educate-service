package com.eduservices.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.eduservices.configuration","com.eduservices.common"})
@EnableDiscoveryClient
public class ConfigurationApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name","configuration-service");
		SpringApplication.run(ConfigurationApplication.class, args);
	}

}
