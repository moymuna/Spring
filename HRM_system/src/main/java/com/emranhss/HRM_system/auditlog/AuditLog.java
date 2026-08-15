package com.emranhss.HRM_system.auditlog;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String entityType;

    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditAction action;

    @Column(length = 255)
    private String actorEmail;

    @Column(length = 30)
    private String actorRole;

    @Column(length = 500)
    private String details;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
