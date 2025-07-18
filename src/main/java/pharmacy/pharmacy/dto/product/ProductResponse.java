package pharmacy.pharmacy.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductResponse {
    private Integer id;
    private String name;
    private String description;  // Added product description
    private String imageUrl;     // Added product image
    private BigDecimal price;
    private BigDecimal costPrice;  // Added cost price
    private String categoryName;
    private Integer categoryId;    // Added category ID
    private String barcode;
    private Integer stockQuantity;
    private Integer reorderLevel;  // Added reorder level
    private LocalDate expiryDate;
    private Boolean isPrescriptionRequired;  // Added prescription requirement
    private String batchNumber;    // Added batch number

    // Optional: Add status indicators
    public String getStockStatus() {
        if (stockQuantity == null) return "UNKNOWN";
        if (stockQuantity <= 0) return "OUT_OF_STOCK";
        if (reorderLevel != null && stockQuantity <= reorderLevel) return "LOW_STOCK";
        return "IN_STOCK";
    }

    // Optional: Add expiry status
    public String getExpiryStatus() {
        if (expiryDate == null) return "NO_EXPIRY";
        if (expiryDate.isBefore(LocalDate.now())) return "EXPIRED";
        if (expiryDate.isBefore(LocalDate.now().plusMonths(1))) return "EXPIRING_SOON";
        return "VALID";
    }
}