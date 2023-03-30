package com.mslfox.cloudStorageServices.service.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService<FileInfoDTO, FileDTO, FileRenameDTO>{
     String upload(MultipartFile multipartFile) throws RuntimeException ;

     List<FileInfoDTO> getFileInfoList(int limit) ;

     String deleteFile(FileDTO fileDTO) throws RuntimeException;

     Resource getFileResource(FileDTO fileDTO);

     String renameFile(FileRenameDTO fileRenameDTO) throws RuntimeException;
}
