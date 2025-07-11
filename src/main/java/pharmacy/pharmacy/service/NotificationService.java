package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.NotificationRepository;
import pharmacy.pharmacy.dto.NotificationDTO;
import pharmacy.pharmacy.entity.Notification;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.enums.NotificationType;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationService(NotificationRepository notificationRepository,
                             UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getAllNotifications() {
        try {
            return notificationRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToNotificationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve notifications", e);
        }
    }

    @Transactional(readOnly = true)
    public NotificationDTO getNotificationById(int id) {
        try {
            Notification notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
            return EntityDtoMapper.convertToNotificationDTO(notification);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve notification with id: " + id, e);
        }
    }

    public Notification getNotificationEntityById(int id) {
        try {
            return notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve notification entity with id: " + id, e);
        }
    }

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        try {
            validateNotificationDTO(notificationDTO);

            User user = userService.getUserEntityById(notificationDTO.getUserId());
            Notification notification = EntityDtoMapper.convertToNotification(notificationDTO, user);

            Notification savedNotification = notificationRepository.save(notification);
            return EntityDtoMapper.convertToNotificationDTO(savedNotification);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create notification", e);
        }
    }

    public void markAsRead(int id) {
        try {
            Notification notification = getNotificationEntityById(id);
            notification.markAsRead();
            notificationRepository.save(notification);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to mark notification as read with id: " + id, e);
        }
    }

    public void markMultipleAsRead(List<Integer> ids) {
        try {
            notificationRepository.markAsRead(ids);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to mark notifications as read", e);
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByUser(int userId) {
        try {
            return notificationRepository.findByUserId(userId).stream()
                    .map(EntityDtoMapper::convertToNotificationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve notifications for user id: " + userId, e);
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotificationsByUser(int userId) {
        try {
            return notificationRepository.findUnreadByUserId(userId).stream()
                    .map(EntityDtoMapper::convertToNotificationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve unread notifications for user id: " + userId, e);
        }
    }

    @Transactional(readOnly = true)
    public long countUnreadNotifications(int userId) {
        try {
            return notificationRepository.countUnreadByUserId(userId);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to count unread notifications for user id: " + userId, e);
        }
    }

    public void deleteNotification(int id) {
        try {
            notificationRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete notification with id: " + id, e);
        }
    }

    public void cleanupOldNotifications(Date cutoffDate) {
        try {
            notificationRepository.deleteOlderThan(cutoffDate);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to cleanup old notifications", e);
        }
    }

    private void validateNotificationDTO(NotificationDTO dto) {
        if (dto.getUserId() == null) {
            throw new GlobalException("User ID is required");
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new GlobalException("Notification title is required");
        }
        if (dto.getMessage() == null || dto.getMessage().trim().isEmpty()) {
            throw new GlobalException("Notification message is required");
        }
        if (dto.getNotificationType() == null) {
            throw new GlobalException("Notification type is required");
        }
    }
}