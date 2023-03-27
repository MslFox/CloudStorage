package com.mslfox.cloudStorageServices.dto.file;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(String hash, MultipartFile file) {
}
