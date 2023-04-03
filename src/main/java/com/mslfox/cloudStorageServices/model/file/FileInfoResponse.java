package com.mslfox.cloudStorageServices.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "file-info", description = "Contains file information")
public final class FileInfoResponse {
    private String filename;
    private Long size;
}
