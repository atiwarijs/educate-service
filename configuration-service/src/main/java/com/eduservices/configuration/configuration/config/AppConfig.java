package com.eduservices.configuration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@Component
public class AppConfig {

    @Value("${rest.template.connect.timeout}")
    Integer CONNECT_TIMEOUT;

    @Value("${rest.template.read.timeout}")
    Integer READ_TIMEOUT;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofMillis(CONNECT_TIMEOUT))
                .readTimeout(Duration.ofMillis(READ_TIMEOUT))
                .build();
    }

}
