package com.mslfox.cloudStorageServices.controller.file;

import com.mslfox.cloudStorageServices.dto.file.FileRenameRequest;
import com.mslfox.cloudStorageServices.dto.file.FileRequest;
import com.mslfox.cloudStorageServices.model.error.ErrorResponse;
import com.mslfox.cloudStorageServices.model.file.FileInfoResponse;
import com.mslfox.cloudStorageServices.service.file.impl.FileServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Files manager", description = "Endpoints for file management operations")
public class FileController {
    private final FileServiceImpl fileServiceImpl;

    @Operation(summary = "Returns list of uploaded files with limit parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of file-info",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FileInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing parameters or input data.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("list")
    public List<FileInfoResponse> limitListUploadedFiles(
            @Parameter(description = "Limit(min=1)number of files returned", required = true)
            @RequestParam("limit") @Min(1) int limit) {
        return fileServiceImpl.getFileInfoList(limit);
    }

    @Operation(summary = "Uploads a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The uploaded file name"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing parameters or input data.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("file")
    public String handleFileUpload(
            @Parameter(description = "The file to upload", required = true, schema = @Schema(type = "string", format = "binary"))
            @RequestParam("file") @Valid @NotNull MultipartFile multipartFile) {
        return fileServiceImpl.upload(multipartFile);
    }

    @Operation(summary = "Deletes a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The deleted file name"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing parameters or input data.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("/file")
    public String delete(
            @Parameter(description = "The file to delete", required = true, schema = @Schema(implementation = FileRequest.class))
            @ModelAttribute @Valid FileRequest fileRequest) {
        return fileServiceImpl.deleteFile(fileRequest);

    }

    @Operation(summary = "Downloads a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reference to file that can be served over HTTP",
                    content = @Content(mediaType = "*/*", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing parameters or input data.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/file")
    @ResponseBody
    public Resource serveFile(
            @Parameter(description = "The file to download", required = true, schema = @Schema(implementation = FileRequest.class))
            @ModelAttribute @Valid FileRequest fileRequest) {
        return fileServiceImpl.getFileResource(fileRequest);
    }

    @Operation(summary = "Renames a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The renamed file name"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing parameters or input data.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping("/file")
    public String handleFileRename(
            @Parameter(description = "The new filename for the file", required = true, example = "newFileName.txt")
            @RequestParam("filename") @NotEmpty String toUploadFilename,
            @Parameter(description = "The file to rename", required = true, schema = @Schema(implementation = FileRequest.class))
            @RequestBody @Valid FileRequest fileRequest) {
        final var fileRenameRequest = FileRenameRequest.builder()
                .newFilename(fileRequest.getFilename())
                .toUploadFilename(toUploadFilename)
                .build();
        return fileServiceImpl.renameFile(fileRenameRequest);
    }
}

