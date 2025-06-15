package pharmacy.pharmacy.dto;

import lombok.Data;
import java.util.Date;

@Data
public class InventoryDTO {
    private Integer id;
    private Integer productId;
    private Integer branchId;
    private String shelfLocation;
    private Integer stockLevel;
    private Integer minimumStockLevel;
    private Integer maximumStockLevel;
    private Date lastRestocked;
    private Date lastUpdated;
    private Boolean expiryAlert;
    private Boolean lowStockAlert;
    private Date createdAt;
}