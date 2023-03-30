package com.mslfox.cloudStorageServices.repository.file;

import com.mslfox.cloudStorageServices.dto.file.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileStorage {
     List<FileInfo> loadFileInfoList(String username, int limit) throws IOException;

     void store(String username, MultipartFile multipartFile) throws IOException;

     void delete(String username, String filename) throws IOException;

     Resource loadAsResource(String username, String filename) throws IOException;


     void updateFile(String username, String toUpdateFilename, String newFileName) throws IOException;

}
