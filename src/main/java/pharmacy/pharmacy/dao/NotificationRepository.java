package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.pharmacy.entity.Notification;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.enums.NotificationType;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    // Find notifications by user
    List<Notification> findByUser(User user);

    // Find notifications by user ID
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByUserId(@Param("userId") Integer userId);

    // Find unread notifications by user
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByUserId(@Param("userId") Integer userId);

    // Find notifications by type
    List<Notification> findByNotificationType(NotificationType type);

    // Find notifications by type and user
    List<Notification> findByNotificationTypeAndUser(NotificationType type, User user);

    // Find notifications created after certain date
    @Query("SELECT n FROM Notification n WHERE n.createdAt > :date")
    List<Notification> findRecentNotifications(@Param("date") Date date);

    // Mark notifications as read
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id IN :ids")
    void markAsRead(@Param("ids") List<Integer> notificationIds);

    // Count unread notifications for user
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")
    long countUnreadByUserId(@Param("userId") Integer userId);

    // Delete notifications older than specified date
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :date")
    void deleteOlderThan(@Param("date") Date date);
}