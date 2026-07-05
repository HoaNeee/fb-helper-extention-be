package com.hoane.fbhelper.fbhelperextentionbe.utils;

import jakarta.persistence.AttributeConverter;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Error converting List<String> to JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to List<String>", e);
        }
    }
}
