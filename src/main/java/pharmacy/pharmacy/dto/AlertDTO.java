package pharmacy.pharmacy.dto;

import lombok.Data;
import pharmacy.pharmacy.enums.AlertStatus;
import pharmacy.pharmacy.enums.AlertType;

import java.util.Date;

@Data
public class AlertDTO {
    private Integer id;
    private Integer productId;
    private Integer branchId;
    private AlertType alertType;
    private String message;
    private Integer triggeredById;
    private boolean resolved;
    private Integer resolvedById;
    private Date createdAt;
    private Date resolvedAt;
    private AlertStatus status;

    // Additional display fields
    private String productName;
    private String branchName;
    private String triggeredByName;
    private String resolvedByName;
    private boolean critical;
}