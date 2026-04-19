package com.eduservices.classes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.eduservices.classes","com.eduservices.common"})
@EnableDiscoveryClient
public class ClassApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name","classes-service");
		SpringApplication.run(ClassApplication.class, args);
	}

}
