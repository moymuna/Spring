package com.emranhss.HRM_system.notification;

import com.emranhss.HRM_system.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private Long currentUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    public List<NotificationResponseDto> mine() {
        return notificationService.listForUser(currentUserId());
    }

    @GetMapping("/mine/unread-count")
    @PreAuthorize("isAuthenticated()")
    public long unreadCount() {
        return notificationService.unreadCount(currentUserId());
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public void markRead(@PathVariable Long id) {
        notificationService.markRead(id, currentUserId());
    }
}
