package com.mslfox.cloudStorageServices.controller.file;

import com.mslfox.cloudStorageServices.dto.file.FileInfo;
import com.mslfox.cloudStorageServices.dto.file.FileRenameRequest;
import com.mslfox.cloudStorageServices.dto.file.FileRequest;
import com.mslfox.cloudStorageServices.service.file.impl.FileServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
public class FileController {
    private final FileServiceImpl fileServiceImpl;

    @GetMapping("list")
    public List<FileInfo> limitListUploadedFiles(@RequestParam("limit") @Min(1) int limit)  {
        return fileServiceImpl.getFileInfoList(limit);
    }

    @PostMapping("file")
    public String handleFileUpload(@RequestParam("file") @Valid @NotNull MultipartFile multipartFile)  {
        return fileServiceImpl.upload(multipartFile);
    }

    @DeleteMapping("file")
    public String delete(@ModelAttribute @Valid FileRequest fileRequest) {
        return fileServiceImpl.deleteFile(fileRequest);

    }

    @GetMapping("file")
    @ResponseBody
    public Resource serveFile(@ModelAttribute @Valid FileRequest fileRequest)  {
        return fileServiceImpl.getFileResource(fileRequest);
    }

    @PutMapping("file")
    public String handleFileRename(@RequestParam("filename") @NotEmpty String toUploadFilename, @RequestBody @Valid FileRequest fileRequest) {
        final var fileRenameRequest = FileRenameRequest.builder()
                .newFilename(fileRequest.getFilename())
                .toUploadFilename(toUploadFilename)
                .build();
        return fileServiceImpl.renameFile(fileRenameRequest);
    }
}

