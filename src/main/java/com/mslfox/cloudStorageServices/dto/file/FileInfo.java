package com.mslfox.cloudStorageServices.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class FileInfo {
    private String filename;
    private Long size;

}
