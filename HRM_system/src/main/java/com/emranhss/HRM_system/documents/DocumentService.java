package com.emranhss.HRM_system.documents;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface DocumentService {
    DocumentsResponseDto createDocument(DocumentsRequestDto dto);

    
    DocumentsResponseDto createDocumentWithFile(DocumentsRequestDto dto, MultipartFile file);

    DocumentsResponseDto getDocumentById(Long id);

    List<DocumentsResponseDto> getAllDocuments();

    DocumentsResponseDto updateDocument(Long id, DocumentsRequestDto dto);

    void deleteDocument(Long id);

    List<DocumentsResponseDto> getDocumentsByEmployeeId(Long employeeId);
}
