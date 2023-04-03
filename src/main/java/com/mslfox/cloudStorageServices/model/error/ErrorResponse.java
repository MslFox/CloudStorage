package com.mslfox.cloudStorageServices.model.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
@Schema(name = "error", description = "Represent an error that may occur when processing request")
public final class ErrorResponse {
    private final String message;
    private final long id;
}
