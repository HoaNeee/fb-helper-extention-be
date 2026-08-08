package com.hoane.fbhelper.fbhelperextentionbe.utils.converter;

import jakarta.persistence.AttributeConverter;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public class ListConverter<T> implements AttributeConverter<List<T>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Error converting List to JSON", e);
        }
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to List", e);
        }
    }
}
