package com.example.snsserver.common.service;

import com.example.snsserver.common.exception.ImageUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class BaseImageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket:my-snsserver-uploads}")
    private String bucketName;

    public BaseImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImage(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            log.debug("No file provided for upload");
            return null;
        }

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String key = subDir + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("Successfully uploaded file to S3: {}/{}", bucketName, key);

            return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, key);
        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage(), e);
            throw new ImageUploadException("File upload to S3 failed: " + e.getMessage(), e);
        }
    }

    public void deleteImage(String fileUrl) {
        if (fileUrl == null) {
            return;
        }

        String key = fileUrl.replaceFirst("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/", "");

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Successfully deleted file from S3: {}/{}", bucketName, key);
        } catch (Exception e) {
            log.warn("Failed to delete file from S3: {}/{}, error: {}", bucketName, key, e.getMessage());
        }
    }
}