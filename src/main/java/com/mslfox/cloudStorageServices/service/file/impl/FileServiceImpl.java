package com.mslfox.cloudStorageServices.service.file.impl;

import com.mslfox.cloudStorageServices.messages.ErrorMessage;
import com.mslfox.cloudStorageServices.model.file.FileInfoResponse;
import com.mslfox.cloudStorageServices.dto.file.FileRenameRequest;
import com.mslfox.cloudStorageServices.dto.file.FileRequest;
import com.mslfox.cloudStorageServices.exception.InternalServerException;
import com.mslfox.cloudStorageServices.messages.SuccessMessage;
import com.mslfox.cloudStorageServices.repository.file.impl.FileSystemStorage;
import com.mslfox.cloudStorageServices.service.file.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class FileServiceImpl implements FileService<FileInfoResponse, FileRequest, FileRenameRequest> {
    private final ErrorMessage errorMessage;
    private final SuccessMessage successMessage;
    private final FileSystemStorage fileSystemStorage;

    @Override
    public String upload(MultipartFile multipartFile) throws RuntimeException {

        final var currentUsername = getCurrentUserName();
        try {
            fileSystemStorage.store(currentUsername, multipartFile);
            return successMessage.upload;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerException(errorMessage.uploadFile);
        }
    }

    @Override
    public List<FileInfoResponse> getFileInfoList(int limit) {
        final var currentUsername = getCurrentUserName();
        try {
            return fileSystemStorage.loadFileInfoList(currentUsername, limit);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerException(errorMessage.gettingFileList);
        }
    }

    @Override
    public String deleteFile(FileRequest fileRequest) throws RuntimeException {
        final var currentUsername = getCurrentUserName();
        try {
            fileSystemStorage.delete(currentUsername, fileRequest.getFilename());
            return successMessage.delete;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerException(errorMessage.deleteFile);
        }
    }

    @Override
    public Resource getFileResource(FileRequest fileRequest) {
        final var currentUsername = getCurrentUserName();
        try {
            return fileSystemStorage.loadAsResource(currentUsername, fileRequest.getFilename());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerException(errorMessage.gettingFile);
        }
    }

    @Override
    public String renameFile(FileRenameRequest fileRenameRequest) throws RuntimeException {
        final var currentUsername = getCurrentUserName();
        try {
            fileSystemStorage.updateFile(
                    currentUsername,
                    fileRenameRequest.getToUploadFilename(),
                    fileRenameRequest.getNewFilename());
            return successMessage.rename;
        } catch (Exception e) {
            throw new InternalServerException(errorMessage.renameFile);
        }
    }

    private String getCurrentUserName() {
        final var username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (username == null || username.length() == 0) {
            throw new RuntimeException(errorMessage.securityInvalidUsername);
        }
        return username;
    }
}


