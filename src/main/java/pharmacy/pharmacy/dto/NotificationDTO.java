package pharmacy.pharmacy.dto;

import lombok.Data;
import pharmacy.pharmacy.enums.NotificationType;

import java.util.Date;

@Data
public class NotificationDTO {
    private Integer id;
    private Integer userId;
    private String title;
    private String message;
    private boolean isRead;
    private NotificationType notificationType;
    private Integer relatedId;
    private Date createdAt;

    // Additional display fields
    private String userName;
    private String relatedEntityType;
}