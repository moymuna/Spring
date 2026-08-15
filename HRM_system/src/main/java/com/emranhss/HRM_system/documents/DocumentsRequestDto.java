package com.emranhss.HRM_system.documents;

import com.emranhss.HRM_system.enums.DocumentType;
import lombok.Data;

@Data
public class DocumentsRequestDto {
    private String documentName;

    private DocumentType documentType;

    private String filePath;

    private Long employeeId;
}
