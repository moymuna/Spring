package com.emranhss.HRM_system.utill;

import com.emranhss.HRM_system.exception.ExternalServiceException;
import com.emranhss.HRM_system.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;


@Service
public class FileStorageService {

    @Value("${image.upload.dir}")
    private String baseDir;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".pdf", ".doc", ".docx"
    );

    
    
    private static final long MAX_SIZE_BYTES = 10L * 1024 * 1024;

    
    public String store(MultipartFile file) {
        return store(file, "", null);
    }

    
    public String store(MultipartFile file, String subfolder) {
        return store(file, subfolder, null);
    }

    
    public String store(MultipartFile file, String subfolder, String namePrefix) {

        if (file == null || file.isEmpty()) {
            throw new ValidationException("File is empty.");
        }

        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new ValidationException("File exceeds the maximum allowed size of 10MB.");
        }

        String original = file.getOriginalFilename();
        String extension = "";
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf(".")).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ValidationException(
                    "File type '" + extension + "' is not allowed. Allowed types: " + ALLOWED_EXTENSIONS);
        }

        try {
            Path dir = Paths.get(baseDir, subfolder);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            String fileName = (namePrefix != null && !namePrefix.isBlank()
                    ? namePrefix.trim().replaceAll("\\s+", "_") + "_"
                    : "") + UUID.randomUUID() + extension;

            Files.copy(file.getInputStream(), dir.resolve(fileName));

            return fileName;
        } catch (IOException e) {
            throw new ExternalServiceException("File upload failed. Please try again shortly.");
        }
    }

    
    public boolean exists(String subfolder, String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return false;
        }
        return Files.exists(Paths.get(baseDir, subfolder, fileName));
    }
}
