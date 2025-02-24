package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.entity.Notification;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.repository.NotificationRepository;
import pharmacy.pharmacy.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    // Fetch all notifications
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Fetch notifications for a specific user
    public List<Notification> getNotificationsByUserId(UUID userId) {
        return notificationRepository.findByUserId(userId);
    }

    // Create a new notification
    public Notification createNotification(UUID userId, String message) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Notification notification = new Notification();
            notification.setUser(userOptional.get());
            notification.setMessage(message);
            notification.setRead(false);
            return notificationRepository.save(notification);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    // Mark a notification as read
    public Notification markAsRead(UUID notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notification.setRead(true);
            return notificationRepository.save(notification);
        } else {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
    }

    // Delete a notification
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
