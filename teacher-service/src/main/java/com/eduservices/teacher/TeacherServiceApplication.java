package com.eduservices.teacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableJpaAuditing
@ComponentScan({"com.eduservices.teacher","com.eduservices.common"})
public class TeacherServiceApplication {

	public static void main(String[] args) {
		System.setProperty("spring.application.name", "TEACHER-SERVICE");
		SpringApplication.run(TeacherServiceApplication.class, args);
	}

}
