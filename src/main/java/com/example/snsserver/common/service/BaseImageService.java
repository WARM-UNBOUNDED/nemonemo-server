package com.example.snsserver.common.service;

import com.example.snsserver.common.exception.ImageUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public abstract class BaseImageService {

    @Value("${file.upload-dir:/uploads}")
    private String baseUploadDir;

    protected String uploadImage(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            log.debug("No file provided for upload");
            return null;
        }

        String uploadDir = baseUploadDir + File.separator + subDir;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                log.error("Failed to create directory: {}", uploadDir);
                throw new ImageUploadException("Failed to create upload directory: " + uploadDir);
            }
        }

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File destFile = new File(dir, fileName);
            log.info("Saving file to: {}", destFile.getAbsolutePath());
            file.transferTo(destFile);
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("Failed to save file: {}", e.getMessage(), e);
            throw new ImageUploadException("File upload failed: " + e.getMessage(), e);
        }
    }

    protected void deleteImage(String filePath) {
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                log.warn("Failed to delete file: {}", filePath);
            } else {
                log.info("Successfully deleted file: {}", filePath);
            }
        }
    }
}