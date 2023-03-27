package com.mslfox.cloudStorageServices.model.Error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public final class ErrorResponse {
    private final String message;
    private final long id;

    public String toJsonString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сериализации объекта в JSON", e);
        }
    }

}
