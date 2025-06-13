package pharmacy.pharmacy.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class StockDTO {
    private Integer id;
    private Integer productId;
    private Integer supplierId;
    private Integer quantityAdded;
    private Double unitCost;
    private LocalDateTime dateAdded;
    private Date expiryDate;
    private String batchNumber;
    private Integer branchId;
    private Integer approvedById;

    // Optional fields for display purposes
    private String productName;
    private String supplierName;
    private String branchName;
    private String approvedByName;
    private Double totalCost; // Calculated field: quantityAdded * unitCost

    // Status fields
    private Boolean expired; // Calculated based on expiryDate
    private Boolean expiringSoon; // Calculated based on expiryDate
}