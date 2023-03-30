package com.mslfox.cloudStorageServices.service.file.impl;

import com.mslfox.cloudStorageServices.dto.file.FileInfo;
import com.mslfox.cloudStorageServices.dto.file.FileRenameRequest;
import com.mslfox.cloudStorageServices.dto.file.FileRequest;
import com.mslfox.cloudStorageServices.exception.InternalServerException;
import com.mslfox.cloudStorageServices.repository.file.impl.FileSystemStorage;
import com.mslfox.cloudStorageServices.service.file.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.mslfox.cloudStorageServices.constant.ConstantsHolder.*;


@Service
@Slf4j
@AllArgsConstructor
public class FileServiceImpl implements FileService<FileInfo, FileRequest, FileRenameRequest> {
    private final FileSystemStorage fileSystemStorage;

    @Override
    public String upload(MultipartFile multipartFile) throws RuntimeException {

        final var currentUsername = getCurrentUserName();
        try {
            fileSystemStorage.store(currentUsername, multipartFile);
            return SUCCESS_UPLOAD;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw InternalServerException.builder()
                    .message(ERROR_UPLOAD_FILE).build();
        }
    }

    @Override
    public List<FileInfo> getFileInfoList(int limit) {
        final var currentUsername = getCurrentUserName();
        try {
            return fileSystemStorage.loadFileInfoList(currentUsername, limit);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw InternalServerException.builder()
                    .message(ERROR_GET_FILE_LIST).build();
        }
    }

    @Override
    public String deleteFile(FileRequest fileRequest) throws RuntimeException {
        final var currentUsername = getCurrentUserName();
        try {
            fileSystemStorage.delete(currentUsername, fileRequest.getFilename());
            return SUCCESS_DELETE;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw InternalServerException.builder()
                    .message(ERROR_DELETE_FILE).build();
        }
    }

    @Override
    public Resource getFileResource(FileRequest fileRequest) {
        final var currentUsername = getCurrentUserName();
        try {
            return fileSystemStorage.loadAsResource(currentUsername, fileRequest.getFilename());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw InternalServerException.builder()
                    .message(ERROR_GET_FILE).build();
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
            return SUCCESS_RENAME;
        } catch (Exception e) {
            throw InternalServerException.builder()
                    .message(ERROR_RENAME_FILE).build();
        }
    }

    private String getCurrentUserName() {
        final var username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (username == null || username.length() == 0) {
            throw new RuntimeException(ERROR_SECURITY_CONTEXT_INVALID_USERNAME);
        }
        return username;
    }
}


