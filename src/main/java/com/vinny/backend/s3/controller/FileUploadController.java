package com.vinny.backend.s3.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "File API", description = "파일(S3) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileUploadController {

    private final S3Service s3Service;

    @Operation(summary = "단일 파일 업로드 API", description = "파일 하나를 S3에 업로드하고 URL을 반환합니다.")
    @PostMapping(value = "/single-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadFile(
            @Parameter(description = "업로드할 파일") @RequestPart("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ApiResponse.onFailure(ErrorStatus.FILE_IS_EMPTY.getCode(), ErrorStatus.FILE_IS_EMPTY.getMessage(), null);
        }

        try {
            String fileUrl = s3Service.uploadFile(file);
            return ApiResponse.onSuccess(fileUrl);
        } catch (IOException e) {
            return ApiResponse.onFailure(ErrorStatus.S3_UPLOAD_FAILED.getCode(), ErrorStatus.S3_UPLOAD_FAILED.getMessage(), null);
        }
    }

    @Operation(summary = "다중 파일 업로드 API", description = "여러 파일을 S3에 동시에 업로드하고 URL 목록을 반환합니다.")
    @PostMapping(value = "/multiple-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<String>> uploadFiles(
            @Parameter(description = "업로드할 파일 목록") @RequestPart("files") List<MultipartFile> files
    ) {
        if (files == null || files.isEmpty()) {
            return ApiResponse.onFailure(ErrorStatus.FILE_IS_EMPTY.getCode(), ErrorStatus.FILE_IS_EMPTY.getMessage(), null);
        }

        try {
            List<String> fileUrls = s3Service.uploadFiles(files);
            return ApiResponse.onSuccess(fileUrls);
        } catch (IOException e) {
            return ApiResponse.onFailure(ErrorStatus.S3_UPLOAD_FAILED.getCode(), ErrorStatus.S3_UPLOAD_FAILED.getMessage(), null);
        }
    }

    @Operation(summary = "파일 삭제 API", description = "S3에 업로드된 파일을 삭제합니다.")
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteFile(
            @Parameter(description = "삭제할 파일의 전체 S3 URL", required = true) @RequestParam("fileUrl") String fileUrl
    ) {
        try {
            s3Service.deleteFile(fileUrl);
            return ApiResponse.onSuccess("File deleted successfully");
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure(ErrorStatus.INVALID_FILE_URL.getCode(), ErrorStatus.INVALID_FILE_URL.getMessage(), null);
        } catch (Exception e) {
            return ApiResponse.onFailure(ErrorStatus.S3_DELETE_FAILED.getCode(), ErrorStatus.S3_DELETE_FAILED.getMessage(), null);
        }
    }
}