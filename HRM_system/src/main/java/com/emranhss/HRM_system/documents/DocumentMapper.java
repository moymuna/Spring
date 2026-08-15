package com.emranhss.HRM_system.documents;

import com.emranhss.HRM_system.employee.Employee;

public class DocumentMapper {
    
    public static Documents toEntity(DocumentsRequestDto dto,
                                     Employee employee) {

        Documents documents = new Documents();

        
        documents.setDocumentName(dto.getDocumentName());
        documents.setDocumentType(dto.getDocumentType());
        documents.setFilePath(dto.getFilePath());

        
        documents.setEmployee(employee);

        return documents;
    }

    
    public static DocumentsResponseDto toResponse(Documents documents) {

        DocumentsResponseDto dto = new DocumentsResponseDto();

        dto.setId(documents.getId());
        dto.setDocumentName(documents.getDocumentName());
        dto.setDocumentType(documents.getDocumentType());
        dto.setFilePath(documents.getFilePath());
        dto.setUploadedAt(documents.getUploadedAt());

        if (documents.getEmployee() != null) {

            dto.setEmployeeId(documents.getEmployee().getId());
            dto.setEmployeeName(documents.getEmployee().getUser().getFullName());
        }

        return dto;
    }

    
    public static void updateEntity(Documents documents,
                                    DocumentsRequestDto dto,
                                    Employee employee) {

        documents.setDocumentName(dto.getDocumentName());
        documents.setDocumentType(dto.getDocumentType());
        documents.setFilePath(dto.getFilePath());

        documents.setEmployee(employee);
    }

}
