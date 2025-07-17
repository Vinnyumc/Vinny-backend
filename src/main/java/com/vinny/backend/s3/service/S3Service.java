package com.vinny.backend.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            fileUrls.add(uploadFile(file));
        }
        return fileUrls;
    }

    public String uploadFile(MultipartFile multipartFile) throws IOException {

        String uniqueFileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(new PutObjectRequest(bucket, uniqueFileName, multipartFile.getInputStream(), metadata));

        return amazonS3Client.getUrl(bucket, uniqueFileName).toString();
    }

    public void deleteFile(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);
        amazonS3Client.deleteObject(bucket, fileName);
    }

    private String extractFileNameFromUrl(String fileUrl) {
        try {
            String encodedFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            return URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file URL", e);
        }
    }
}
