package com.eduservices.registrations.service.connector;

import com.eduservices.registrations.dto.ConfigurationDto;
import com.eduservices.registrations.dto.SearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class ConfigurationConnector {

    @Autowired
    RestTemplate restTemplate;

    @Value("${service.configuration.base-url}")
    String BASE_URL;

    public List<ConfigurationDto> getConfigurations(String query, Long orgId){
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL + "/configurations/search");

            SearchDto dto = new SearchDto();
            dto.setKey(query);
            dto.setOrgId(orgId);

            URI uri = builder.build().toUri();

            ResponseEntity<List<ConfigurationDto>> response = restTemplate.exchange(
                    uri, HttpMethod.POST, new org.springframework.http.HttpEntity<>(dto),
                    new ParameterizedTypeReference<List<ConfigurationDto>>() {
                    }
            );
            return response.getBody();
        } catch(Exception e){
            log.error("Error: {} ",e.getLocalizedMessage());
            return List.of();
        }
    }
}
