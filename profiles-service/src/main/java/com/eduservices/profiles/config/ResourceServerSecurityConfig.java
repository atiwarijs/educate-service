package com.eduservices.profiles.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ResourceServerSecurityConfig {
	
	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/api/v1/profile/**", "/personal/**").authenticated()
	                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
	                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
					.requestMatchers("/actuator/**").hasRole("ADMIN")
	                .anyRequest().authenticated()
	            )
	            .httpBasic(basic -> {})
	            .oauth2ResourceServer(oauth2 -> oauth2
	                .jwt(Customizer.withDefaults())
	            );

	        return http.build();
	    }

		@Bean
		public JwtDecoder jwtDecoder() {
			return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
		}
}
