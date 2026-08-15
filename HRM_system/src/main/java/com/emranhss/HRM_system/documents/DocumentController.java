package com.emranhss.HRM_system.documents;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR','MANAGER')")
    public ResponseEntity<DocumentsResponseDto> create(@RequestBody DocumentsRequestDto dto) {

        return ResponseEntity.ok(documentService.createDocument(dto));
    }

    
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER') or @employeeSecurity.isOwnerOrNotEmployee(#dto.employeeId)")
    public ResponseEntity<DocumentsResponseDto> createWithFile(
            @RequestPart("document") DocumentsRequestDto dto,
            @RequestPart("file") MultipartFile file) {

        return ResponseEntity.ok(documentService.createDocumentWithFile(dto, file));
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("@employeeSecurity.isDocumentOwnerOrNotEmployee(#id)")
    public ResponseEntity<DocumentsResponseDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<DocumentsResponseDto>> getAll() {

        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR','MANAGER')")
    public ResponseEntity<DocumentsResponseDto> update(
            @PathVariable Long id,
            @RequestBody DocumentsRequestDto dto) {

        return ResponseEntity.ok(documentService.updateDocument(id, dto));
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        documentService.deleteDocument(id);
        return ResponseEntity.ok("Document deleted successfully");
    }

    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("@employeeSecurity.isOwnerOrNotEmployee(#employeeId)")
    public ResponseEntity<List<DocumentsResponseDto>> getByEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(documentService.getDocumentsByEmployeeId(employeeId));
    }
}
