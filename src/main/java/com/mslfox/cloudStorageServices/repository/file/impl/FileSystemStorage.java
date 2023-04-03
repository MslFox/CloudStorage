package com.mslfox.cloudStorageServices.repository.file.impl;

import com.mslfox.cloudStorageServices.model.file.FileInfoResponse;
import com.mslfox.cloudStorageServices.repository.file.FileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class FileSystemStorage implements FileStorage {
    private final Path storagePath;

    public FileSystemStorage(@Value("${file.system.storage.location:uploads/}") String storageLocation) throws IOException {
        this.storagePath = initFolder(Path.of(storageLocation));
        log.info("File storage initialized");
        log.info("File storage type: Local Filesystem");
        log.info("File storage location: {}", storagePath.toUri());
    }

    @Override
    public List<FileInfoResponse> loadFileInfoList(String username, int limit) throws IOException {
        final File files = getUserFolderPath(username).toFile();
        return Arrays.stream(Objects.requireNonNull(files.listFiles()))
                .sorted(Comparator.comparingLong(File::lastModified).reversed())
                .limit(limit)
                .map(file -> new FileInfoResponse(file.getName(), file.length()))
                .toList();
    }


    @Override
    public void store(String username, MultipartFile multipartFile) throws IOException {
        final var filename = multipartFile.getOriginalFilename();
        if (filename != null) {
            final var filePath = getUserFolderPath(username).resolve(filename);
            Files.write(filePath, multipartFile.getBytes());
            log.info("User '{}' save file '{}'",
                    username,
                    filename);
        } else throw new IOException();
    }

    @Override
    public void delete(String username, String filename) throws IOException {
        final var filePath = getUserFolderPath(username).resolve(filename);
        Files.delete(filePath);
        log.info("User '{}' delete file '{}'",
                username,
                filename);
    }

    @Override
    public Resource loadAsResource(String username, String filename) throws IOException {
        final var filePath = getUserFolderPath(username).resolve(filename);
        return new UrlResource(filePath.toUri());
    }

    @Override
    public void updateFile(String username, String toUpdateFilename, String newFileName) throws IOException {
        final var fileFolderPath = getUserFolderPath(username);
        final var toUpdateFilePath = fileFolderPath.resolve(toUpdateFilename);
        final var newFilePath = fileFolderPath.resolve(newFileName);
        Files.move(toUpdateFilePath, newFilePath);
        log.info("User '{}' change filename '{}' to '{}'",
                username,
                toUpdateFilename,
                newFileName);
    }

    private Path initFolder(Path folderName) throws IOException {
        return Files.createDirectories(folderName);
    }

    private Path getUserFolderPath(String username) throws IOException {
        return initFolder(storagePath.resolve(username));
    }
}
