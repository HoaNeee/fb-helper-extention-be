package com.hoane.fbhelper.fbhelperextentionbe.utils.converter;

import jakarta.persistence.AttributeConverter;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public abstract class BaseListConverter<T> implements AttributeConverter<List<T>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JavaType javaType;

    public BaseListConverter(Class<T> tClass) {
        this.javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, tClass);
    }

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Error converting List to JSON", e);
        }
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) return null;
            return objectMapper.readValue(dbData, javaType);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to List", e);
        }
    }
}
