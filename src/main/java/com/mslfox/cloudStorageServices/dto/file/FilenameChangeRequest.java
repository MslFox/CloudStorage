package com.mslfox.cloudStorageServices.dto.file;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class FilenameChangeRequest {

    private final String newFilename;
    private final String toUploadFilename;
}
