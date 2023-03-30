package com.mslfox.cloudStorageServices.dto.file;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FileRequest {
    @NotEmpty
    private String filename;
}
