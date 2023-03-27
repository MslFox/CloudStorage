package com.mslfox.cloudStorageServices.service.file;

import com.mslfox.cloudStorageServices.dto.file.FileUploadRequest;
import com.mslfox.cloudStorageServices.dto.file.FilenameChangeRequest;
import com.mslfox.cloudStorageServices.dto.file.FilenameRequest;
import com.mslfox.cloudStorageServices.exception.DetectedException;
import com.mslfox.cloudStorageServices.exception.InternalServerException;
import com.mslfox.cloudStorageServices.model.file.FileGetResponse;
import com.mslfox.cloudStorageServices.model.file.FileInfoResponse;
import com.mslfox.cloudStorageServices.repository.file.FileSystemRepository;
import com.mslfox.cloudStorageServices.util.FileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mslfox.cloudStorageServices.constant.ConstantsHolder.*;


@Service
@Slf4j
@AllArgsConstructor
public class FileService {
    private final FileSystemRepository fileSystemRepository;
    private final FileUtil fileUtil;

    public String upload(FileUploadRequest fileUploadRequest) throws DetectedException {
        final var currentUsername = getCurrentUserName();
        final var filename = fileUploadRequest.file().getOriginalFilename();
        final var multipartFile = fileUploadRequest.file();
        try {
            fileSystemRepository.saveFile(currentUsername, filename, multipartFile);
            return "File uploaded successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw getInternalServerException(ERROR_UPLOADING_FILE);
        }
    }

    public List<FileInfoResponse> getFileInfoList(int limit) {
        final var currentUsername = getCurrentUserName();
        try {
            return fileSystemRepository.getFileInfoList(currentUsername, limit);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw getInternalServerException(ERROR_GETTING_FILE_LIST);
        }
    }

    public String deleteFile(FilenameRequest filenameRequest) throws DetectedException {
        try {
            fileSystemRepository.deleteFile(getCurrentUserName(), filenameRequest.getFilename());
            return "Success delete";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw getInternalServerException(ERROR_DELETING_FILE);
        }
    }

    public FileGetResponse getFile(FilenameRequest filenameRequest) {
        try {
            byte[] fileBytes = fileSystemRepository.getFile(getCurrentUserName(), filenameRequest.getFilename());
            return new FileGetResponse(fileUtil.getHash(fileBytes),fileBytes);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw getInternalServerException(ERROR_GETTING_FILE);
        }
    }

    public String changeFilename(FilenameChangeRequest filenameChangeRequest) throws DetectedException {
        try {
            fileSystemRepository.updateFile(
                    getCurrentUserName(),
                    filenameChangeRequest.getToUploadFilename(),
                    filenameChangeRequest.getNewFilename());
            return "File uploaded successfully";
        } catch (Exception e) {
            throw getInternalServerException(ERROR_CHANGING_FILENAME);
        }
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    public InternalServerException getInternalServerException(String message) throws InternalServerException {
        return InternalServerException.builder()
                .message(message).build();
    }
}


