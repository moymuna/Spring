package com.emranhss.HRM_system.auditlog;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-log")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AuditLog> getRecent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return auditLogService.getRecent(PageRequest.of(page, size));
    }

    @GetMapping("/latest")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public List<AuditLog> getLatest() {
        return auditLogService.getLatest();
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getForEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return auditLogService.getForEntity(entityType, entityId);
    }
}
