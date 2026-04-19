package com.eduservices.profile.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.eduservices.profile.keycloak.KeycloakClient;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

/**
 * Configuration for Feign clients
 */
@Configuration
@EnableFeignClients(clients = KeycloakClient.class)
public class FeignClientConfig {

	/**
	 * Set up form encoder for form-urlencoded requests
	 */
	@Bean
	public Encoder feignFormEncoder() {
		return new SpringFormEncoder(new SpringEncoder(new ObjectFactory<HttpMessageConverters>() {
			@Override
			public HttpMessageConverters getObject() {
				return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
			}
		}));
	}

	/**
	 * Set up logging level for Feign clients
	 */
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
