package com.mslfox.cloudStorageServices.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class FilenameRequest {
    @NotEmpty
    private String filename;
}
