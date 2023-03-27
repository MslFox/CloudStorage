package com.mslfox.cloudStorageServices.model.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public final class FileGetResponse {
    private final String hash;
    private final byte[] file;

}
