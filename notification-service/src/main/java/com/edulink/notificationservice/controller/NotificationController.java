package com.edulink.notificationservice.controller;
import com.edulink.notificationservice.dto.ApiResponse;
import com.edulink.notificationservice.entity.Notification;
import com.edulink.notificationservice.repository.NotificationRepository;
<<<<<<< HEAD
import com.edulink.notificationservice.exception.InvalidNotificationException;
import com.edulink.notificationservice.exception.NotificationNotFoundException;
=======
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
import jakarta.validation.Valid;
=======
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN')")
<<<<<<< HEAD
    public ResponseEntity<ApiResponse<Notification>> sendNotification(@Valid @RequestBody Notification notification) {
        if (notification == null) {
            throw new InvalidNotificationException("Notification payload must not be null");
        }
        if (notification.getRecipientEmail() == null || notification.getRecipientEmail().trim().isEmpty()) {
            throw new InvalidNotificationException("Recipient email is required");
        }
        String email = notification.getRecipientEmail().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidNotificationException("Recipient email is invalid");
        }
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            throw new InvalidNotificationException("Notification title is required");
        }
=======
    public ResponseEntity<ApiResponse<Notification>> sendNotification(@RequestBody Notification notification) {
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        log.info("Sending notification to: {} (id: {}, role: {})", notification.getRecipientEmail(), notification.getRecipientId(), notification.getRecipientRole());
        Notification saved = repo.save(notification);
        log.info("Notification saved with id: {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification sent", saved));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Notification>>> getMyNotifications(Authentication auth) {
        log.info("Fetching notifications for user: {}", auth.getName());
        List<Notification> notifications = repo.findByRecipientEmailAndReadStatusFalse(auth.getName());
        log.info("Found {} notifications by email", notifications.size());
        if (notifications.isEmpty()) {
            Object userId = auth.getDetails();
            if (userId != null) {
                notifications = repo.findByRecipientIdAndReadStatusFalse(String.valueOf(userId));
                log.info("Fallback: found {} notifications by id", notifications.size());
            }
        }
        return ResponseEntity.ok(ApiResponse.success("Notifications", notifications));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Notification>> markRead(@PathVariable Long id) {
        return repo.findById(id).map(n -> { n.setReadStatus(true); return ResponseEntity.ok(ApiResponse.success("Marked as read", repo.save(n))); })
<<<<<<< HEAD
                .orElseThrow(() -> new NotificationNotFoundException("Notification with id " + id + " not found"));
=======
                .orElseThrow(() -> new RuntimeException("Not found"));
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    }
}
