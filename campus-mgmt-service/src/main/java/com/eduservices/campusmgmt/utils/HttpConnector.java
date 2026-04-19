package com.eduservices.campusmgmt.utils;

import com.eduservices.campusmgmt.exception.OperationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@Slf4j
public class HttpConnector {
    
    private static final Logger log = LoggerFactory.getLogger(HttpConnector.class);

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<?> executePost(String url, HttpEntity<?> entity, Object responseType){
        try {
            URI uri = new URI(url);
            return restTemplate.postForEntity(uri, entity, responseType.getClass());
        } catch (Exception e){
            log.error("ExecutePost Error: {} ", e.getMessage());
            throw new OperationFailedException(e.getMessage());
        }
    }

    public ResponseEntity<?> execute(String url, HttpEntity<?> entity, HttpMethod method, Object responseType){
        try {
            URI uri = new URI(url);
            return restTemplate.exchange(uri, method, entity, responseType.getClass());
        } catch (Exception e){
            log.error("Execute {} Error: {} ",method.name(), e.getMessage());
            throw new OperationFailedException(e.getMessage());
        }
    }
}
