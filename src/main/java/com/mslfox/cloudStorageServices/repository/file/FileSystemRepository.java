package com.mslfox.cloudStorageServices.repository.file;

import com.mslfox.cloudStorageServices.model.file.FileInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class FileSystemRepository {
    private final String storagePath;

    public FileSystemRepository(@Value("${file.storage.location:defaultStorage/}") String storagePath) {
        this.storagePath = storagePath;
    }

    public List<FileInfoResponse> getFileInfoList(String username, int limit) throws IOException {
        File folder = getOrCreatePath(username).toFile();
        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .sorted(Comparator.comparingLong(File::lastModified).reversed())
                .limit(limit)
                .map(file -> new FileInfoResponse(file.getName(), file.length()))
                .toList();
    }

    public void saveFile(String username, String filename, MultipartFile file) throws IOException {
        var fileFolderPath = getOrCreatePath(username);
        var filePath = fileFolderPath.resolve(Path.of(filename));
        Files.write(filePath, file.getBytes());
        log.info("User '{}' save file '{}'",
                username,
                filename);
    }

    public void deleteFile(String username, String filename) throws IOException {
        var fileFolderPath = getOrCreatePath(username);
        var filePath = fileFolderPath.resolve(Path.of(filename));
        Files.delete(filePath);
        log.info("User '{}' delete file '{}'",
                username,
                filename);
    }

    public byte[] getFile(String username, String filename) throws IOException {
        var fileFolderPath = getOrCreatePath(username);
        var filePath = fileFolderPath.resolve(Path.of(filename));
        return Files.readAllBytes(filePath);
    }

    public void updateFile(String username, String toUpdateFilename, String newFileName) throws IOException {
        var fileFolderPath = getOrCreatePath(username);
        var toUpdateFilePath = fileFolderPath.resolve(Path.of(toUpdateFilename));
        var newFilePath = fileFolderPath.resolve(Path.of(newFileName));
        Files.move(toUpdateFilePath, newFilePath);
        log.info("User '{}' change filename '{}' to '{}'",
                username,
                toUpdateFilename,
                newFileName);
    }

    private Path getOrCreatePath(String folderName) throws IOException {
        var storageFolderPath = Path.of(storagePath).resolve(Path.of(folderName));
        if (!Files.exists(storageFolderPath) || !Files.isDirectory(storageFolderPath)) {
            Files.createDirectories(storageFolderPath);

        }
        return storageFolderPath;
    }


}
