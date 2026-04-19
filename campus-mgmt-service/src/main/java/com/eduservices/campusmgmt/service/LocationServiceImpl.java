package com.eduservices.campusmgmt.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eduservices.campusmgmt.dto.StateDTO;
import com.eduservices.campusmgmt.exception.OperationFailedException;
import com.eduservices.campusmgmt.utils.HttpConnector;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {
    
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

	@Autowired
	HttpConnector httpConnector;

	@Autowired
	RestTemplate restTemplate;

	@Value("${personal.base.url}")
	String profileApiUrl;

	public List<StateDTO> fetchAllStates() {
        try {
            ResponseEntity<List<StateDTO>> responseEntity = restTemplate.exchange(new URI(this.profileApiUrl+"/states/list"), HttpMethod.GET, null, new ParameterizedTypeReference<List<StateDTO>>(){});
            if (responseEntity.getStatusCode().value() == 200 || responseEntity.getStatusCode().value() == 204) {
                return responseEntity.getBody();
            }else {
                log.error("All state fetch failed!, Error : {} {} ", responseEntity.getStatusCode().value(), responseEntity.getBody());
                throw new OperationFailedException("State fetch failed!");
            }
        } catch (Exception e){
            log.error("Execute fetchAllStates failed!, Error: {} ", e.getMessage());
            throw new OperationFailedException(e.getMessage());
        }
    }
}
