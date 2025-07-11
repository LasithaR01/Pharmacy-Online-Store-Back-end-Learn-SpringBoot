package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pharmacy.pharmacy.enums.NotificationType;

import java.util.Date;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_notification_user"))
    private User user;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "related_id")
    private Integer relatedId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    // Business logic methods
    public void markAsRead() {
        this.isRead = true;
    }

    public boolean isRelatedToOrder() {
        return notificationType == NotificationType.ORDER;
    }

    public boolean isRelatedToPrescription() {
        return notificationType == NotificationType.PRESCRIPTION;
    }

    public boolean isSystemNotification() {
        return notificationType == NotificationType.SYSTEM;
    }

    public boolean isStockNotification() {
        return notificationType == NotificationType.STOCK;
    }
}