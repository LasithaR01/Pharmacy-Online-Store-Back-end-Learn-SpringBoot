package pharmacy.pharmacy.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierDTO {
    private Integer id;
    private Integer userId;
    private String companyName;
    private String address;
    private String taxId;
    private LocalDateTime createdAt;

    // Additional fields for UI/statistics
    private Integer totalStockItems;
    private String userEmail;  // Will be populated in service layer

    // Validation groups if needed
    public interface CreateValidation {}
    public interface UpdateValidation {}
}