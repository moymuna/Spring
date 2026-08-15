package com.emranhss.HRM_system.notification;

import com.emranhss.HRM_system.exception.ResourceNotFoundException;

import com.emranhss.HRM_system.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    
    public void notify(User recipient, String message, String relatedEntityType, Long relatedEntityId) {
        try {
            if (recipient == null) {
                return;
            }
            Notification notification = new Notification();
            notification.setRecipient(recipient);
            notification.setMessage(message);
            notification.setRelatedEntityType(relatedEntityType);
            notification.setRelatedEntityId(relatedEntityId);
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.warn("Failed to create notification for user {}: {}",
                    recipient != null ? recipient.getId() : null, e.getMessage());
        }
    }

    public List<NotificationResponseDto> listForUser(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public long unreadCount(Long userId) {
        return notificationRepository.countByRecipientIdAndReadFalse(userId);
    }

    public void markRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getRecipient().getId().equals(userId)) {
            
            
            throw new AccessDeniedException("You cannot modify another user's notification.");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private NotificationResponseDto toResponse(Notification n) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(n.getId());
        dto.setMessage(n.getMessage());
        dto.setRelatedEntityType(n.getRelatedEntityType());
        dto.setRelatedEntityId(n.getRelatedEntityId());
        dto.setRead(n.isRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }
}
