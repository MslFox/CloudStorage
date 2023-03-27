package com.mslfox.cloudStorageServices.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class FileInfoResponse {
    private String filename;
    private Long size;

}
