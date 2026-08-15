package com.emranhss.HRM_system.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDto {
    private Long id;
    private String message;
    private String relatedEntityType;
    private Long relatedEntityId;
    private boolean read;
    private LocalDateTime createdAt;
}
