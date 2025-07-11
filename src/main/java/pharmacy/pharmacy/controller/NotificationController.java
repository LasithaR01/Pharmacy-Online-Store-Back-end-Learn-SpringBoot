package pharmacy.pharmacy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.sentry.Sentry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.NotificationDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.NotificationService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Management", description = "Endpoints for managing notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Get all notifications", description = "Retrieve a list of all notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        try {
            return ResponseEntity.ok(notificationService.getAllNotifications());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve notifications", e);
        }
    }

    @Operation(summary = "Get notification by ID", description = "Retrieve a specific notification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(
            @Parameter(description = "ID of the notification to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(notificationService.getNotificationById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve notification with id: " + id, e);
        }
    }

    @Operation(summary = "Create notification", description = "Create a new notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(
            @Parameter(description = "Notification data to be created") @RequestBody NotificationDTO notificationDTO) {
        try {
            return ResponseEntity.ok(notificationService.createNotification(notificationDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create notification", e);
        }
    }

    @Operation(summary = "Mark notification as read", description = "Mark a notification as read by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @Parameter(description = "ID of the notification to mark as read") @PathVariable int id) {
        try {
            notificationService.markAsRead(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to mark notification as read with id: " + id, e);
        }
    }

    @Operation(summary = "Get user notifications", description = "Retrieve all notifications for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notifications",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUser(
            @Parameter(description = "ID of the user") @PathVariable int userId) {
        try {
            return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve notifications for user id: " + userId, e);
        }
    }

    @Operation(summary = "Get unread notifications", description = "Retrieve unread notifications for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved unread notifications",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotificationsByUser(
            @Parameter(description = "ID of the user") @PathVariable int userId) {
        try {
            return ResponseEntity.ok(notificationService.getUnreadNotificationsByUser(userId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve unread notifications for user id: " + userId, e);
        }
    }

    @Operation(summary = "Count unread notifications", description = "Count unread notifications for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully counted unread notifications",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> countUnreadNotifications(
            @Parameter(description = "ID of the user") @PathVariable int userId) {
        try {
            return ResponseEntity.ok(notificationService.countUnreadNotifications(userId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to count unread notifications for user id: " + userId, e);
        }
    }

    @Operation(summary = "Delete notification", description = "Delete a notification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID of the notification to delete") @PathVariable int id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete notification with id: " + id, e);
        }
    }
}