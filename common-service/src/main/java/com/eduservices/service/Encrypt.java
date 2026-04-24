package com.eduservices.service;

import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;

public class Encrypt implements AttributeConverter<String,String> {

    @Autowired
    SyncEncryption encryptionUtil;

    @Override
    public String convertToDatabaseColumn(String data) {
        return data != null ? encryptionUtil.encrypt(data) : "N/A";
    }

    @Override
    public String convertToEntityAttribute(String data) {
        return data != null ? encryptionUtil.decrypt(data) : "N/A";
    }
}
