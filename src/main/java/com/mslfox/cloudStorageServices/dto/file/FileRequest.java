package com.mslfox.cloudStorageServices.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Schema(name = "filename", description = "Contains non empty filename")
public class FileRequest {
    @NotEmpty
    private String filename;
}
