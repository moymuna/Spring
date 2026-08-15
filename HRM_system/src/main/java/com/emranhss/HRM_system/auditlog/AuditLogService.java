package com.emranhss.HRM_system.auditlog;

import com.emranhss.HRM_system.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    
    public void record(String entityType, Long entityId, AuditAction action, String details) {
        try {
            AuditLog entry = new AuditLog();
            entry.setEntityType(entityType);
            entry.setEntityId(entityId);
            entry.setAction(action);
            entry.setDetails(details);
            entry.setTimestamp(LocalDateTime.now());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof User user) {
                entry.setActorEmail(user.getEmail());
                entry.setActorRole(user.getRole().name());
            } else {
                entry.setActorEmail("system");
            }

            auditLogRepository.save(entry);
        } catch (Exception e) {
            log.warn("Failed to write audit log entry for {}#{}: {}", entityType, entityId, e.getMessage());
        }
    }

    
    public void recordForActor(String actorEmail, String entityType, Long entityId,
                                AuditAction action, String details) {
        try {
            AuditLog entry = new AuditLog();
            entry.setEntityType(entityType);
            entry.setEntityId(entityId);
            entry.setAction(action);
            entry.setDetails(details);
            entry.setActorEmail(actorEmail);
            entry.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(entry);
        } catch (Exception e) {
            log.warn("Failed to write audit log entry for {}#{}: {}", entityType, entityId, e.getMessage());
        }
    }

    public Page<AuditLog> getRecent(Pageable pageable) {
        return auditLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    public List<AuditLog> getLatest() {
        return auditLogRepository.findTop10ByOrderByTimestampDesc();
    }

    public List<AuditLog> getForEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);
    }
}
