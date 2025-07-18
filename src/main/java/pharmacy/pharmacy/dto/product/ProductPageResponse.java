package pharmacy.pharmacy.dto.product;

import lombok.Data;
import pharmacy.pharmacy.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductPageResponse {
    private Integer id;
    private String name;
    private String description;
    private String imageUrl;  // Added image URL
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private String batchNumber;
    private String barcode;
    private Boolean isPrescriptionRequired;
    private Integer categoryId;
    private String categoryName;  // Added category name for better UX

    public ProductPageResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();  // Map image URL
        this.price = product.getPrice();
        this.costPrice = product.getCostPrice();  // Fixed: was using price instead of costPrice
        this.stockQuantity = product.getStockQuantity();
        this.reorderLevel = product.getReorderLevel();
        this.expiryDate = product.getExpiryDate();
        this.batchNumber = product.getBatchNumber();
        this.barcode = product.getBarcode();
        this.isPrescriptionRequired = product.getIsPrescriptionRequired();

        // Enhanced category mapping
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
            this.categoryName = product.getCategory().getName();  // Added category name
        }
    }
}