package com.mslfox.cloudStorageServices.controller.file;

import com.mslfox.cloudStorageServices.dto.file.FileUploadRequest;
import com.mslfox.cloudStorageServices.dto.file.FilenameChangeRequest;
import com.mslfox.cloudStorageServices.dto.file.FilenameRequest;
import com.mslfox.cloudStorageServices.model.file.FileInfoResponse;
import com.mslfox.cloudStorageServices.service.file.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("list")
    public List<FileInfoResponse> list(@RequestParam("limit") @Min(1) int limit) {
        return fileService.getFileInfoList(limit);
    }

    @PostMapping("file")
    public String uploadFile(@ModelAttribute FileUploadRequest fileUploadRequest) throws Exception {
        return fileService.upload(fileUploadRequest);
    }

    @DeleteMapping("file")
    public String deleteFile(@ModelAttribute @Valid FilenameRequest filenameRequest) throws Exception {
        return fileService.deleteFile(filenameRequest);
    }

    @GetMapping("file")
    public byte[] getFile(@ModelAttribute @Valid FilenameRequest filenameRequest) throws Exception {
        return fileService.getFile(filenameRequest).getFile();
    }

    @PutMapping("file")
    public String changeFileName(@RequestParam("filename") @NotEmpty String toUploadFilename, @RequestBody @Valid FilenameRequest filenameRequest) throws Exception {
        var filenameChangeRequest = FilenameChangeRequest.builder()
                .newFilename(filenameRequest.getFilename())
                .toUploadFilename(toUploadFilename)
                .build();
        return fileService.changeFilename(filenameChangeRequest);
    }
}

