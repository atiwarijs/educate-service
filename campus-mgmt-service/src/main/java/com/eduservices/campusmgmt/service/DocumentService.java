package com.eduservices.campusmgmt.service;

import com.eduservices.campusmgmt.dto.DocumentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface DocumentService {

    public DocumentDto documentUpload(DocumentDto document, String refIdPrefix);
}
