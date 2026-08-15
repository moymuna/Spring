package com.emranhss.HRM_system.documents;

import com.emranhss.HRM_system.enums.DocumentType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentsResponseDto {
    private Long id;

    private String documentName;

    private DocumentType documentType;

    private String filePath;

    private LocalDateTime uploadedAt;

    private Long employeeId;

    private String employeeName;
}
