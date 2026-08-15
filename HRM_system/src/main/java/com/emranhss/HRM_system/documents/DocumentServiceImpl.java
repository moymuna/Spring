package com.emranhss.HRM_system.documents;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;


import com.emranhss.HRM_system.employee.Employee;
import com.emranhss.HRM_system.employee.EmployeeRepository;
import com.emranhss.HRM_system.utill.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;
    private final FileStorageService fileStorageService;

    
    @Override
    public DocumentsResponseDto createDocument(DocumentsRequestDto dto) {

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Documents document = DocumentMapper.toEntity(dto, employee);

        
        document.setUploadedAt(LocalDateTime.now());

        document = documentRepository.save(document);

        return DocumentMapper.toResponse(document);
    }

    
    @Override
    public DocumentsResponseDto createDocumentWithFile(DocumentsRequestDto dto, MultipartFile file) {
        String storedFileName = fileStorageService.store(file, "documents");
        dto.setFilePath("documents/" + storedFileName);
        return createDocument(dto);
    }

    
    @Override
    @Transactional(readOnly = true)
    public DocumentsResponseDto getDocumentById(Long id) {

        Documents document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        return DocumentMapper.toResponse(document);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentsResponseDto> getAllDocuments() {

        return documentRepository.findAll()
                .stream()
                .map(DocumentMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public DocumentsResponseDto updateDocument(Long id, DocumentsRequestDto dto) {

        Documents document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        DocumentMapper.updateEntity(document, dto, employee);

        document = documentRepository.save(document);

        return DocumentMapper.toResponse(document);
    }

    
    @Override
    public void deleteDocument(Long id) {

        Documents document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        documentRepository.delete(document);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentsResponseDto> getDocumentsByEmployeeId(Long employeeId) {

        return documentRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(DocumentMapper::toResponse)
                .collect(Collectors.toList());
    }

}
