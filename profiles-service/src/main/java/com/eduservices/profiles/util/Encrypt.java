package com.eduservices.profiles.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class Encrypt implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        // For now, return as-is. In a real implementation, you would encrypt here
        return attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        // For now, return as-is. In a real implementation, you would decrypt here
        return dbData;
    }
}
