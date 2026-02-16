package com.ecotrack.api.domain.converter;

import com.ecotrack.api.service.utils.RecordMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RecordMetadataConverter implements AttributeConverter<RecordMetadata, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(RecordMetadata attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Unable to serialize RecordMetadata", ex);
        }
    }

    @Override
    public RecordMetadata convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(dbData, RecordMetadata.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Unable to deserialize RecordMetadata", ex);
        }
    }
}
