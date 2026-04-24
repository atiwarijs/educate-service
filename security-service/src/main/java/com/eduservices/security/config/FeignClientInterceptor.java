package com.eduservices.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignClientInterceptor {

	@Bean
	public RequestInterceptor requestTokenBearerInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate template) {
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();

				if (attributes != null) {
					HttpServletRequest request = attributes.getRequest();
					String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
					if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
						template.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
					}
				}
			}
		};
	}
}
