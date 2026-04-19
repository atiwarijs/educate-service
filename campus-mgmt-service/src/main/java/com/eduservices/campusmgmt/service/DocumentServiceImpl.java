package com.eduservices.campusmgmt.service;

import com.eduservices.campusmgmt.dto.DocumentDto;
import com.eduservices.campusmgmt.dto.PersonalDetailsDto;
import com.eduservices.campusmgmt.utils.HttpConnector;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.security.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Set;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {
    
    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    HttpConnector httpConnector;

    @Value("${document.base.url}")
    String documentBaseUrl;

    @Override
    public DocumentDto documentUpload(DocumentDto document, String refIdPrefix) {
        DocumentDto docDto = null;
        try {
            Instant currentTimestamp = Instant.now();
            document.setRefId(refIdPrefix + "-" + new Date().getTime());
            HttpHeaders docHeaders = new HttpHeaders();
            docHeaders.setContentType(MediaType.APPLICATION_JSON);
            docHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<DocumentDto> docEntity = new HttpEntity<>(document, docHeaders);
            ResponseEntity<?> docResponseEntity = httpConnector.executePost(this.documentBaseUrl+"/upload", docEntity, DocumentDto.class);
            docDto = (DocumentDto) docResponseEntity.getBody();
            assert docDto != null;
            if (docResponseEntity.getStatusCode().value() == 200) {
                log.info("Document {} upload successfully!", docDto.getDocName());
            } else {
                log.error("Document upload failed!, Error : {} {} ", docResponseEntity.getStatusCode().value(), docResponseEntity.getBody());
            }
        } catch(Exception e){
            log.error("Document upload failed!, Error : {} ", e.getMessage());
        }
        return docDto;
    }
}
