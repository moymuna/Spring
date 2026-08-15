package com.emranhss.HRM_system.utill;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService fileStorageService;

    
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE', 'APPLICANT')")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.store(file);
        return ResponseEntity.ok(Map.of("filePath", fileName));
    }
}
