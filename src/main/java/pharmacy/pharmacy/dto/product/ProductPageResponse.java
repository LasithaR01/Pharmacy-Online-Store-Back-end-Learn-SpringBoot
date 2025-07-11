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
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private String batchNumber;
    private String barcode;
    private Boolean isPrescriptionRequired;
    private Integer categoryId;

    public ProductPageResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.costPrice = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.reorderLevel = product.getReorderLevel();
        this.expiryDate = product.getExpiryDate();
        this.batchNumber = product.getBatchNumber();
        this.barcode = product.getBarcode();
        this.categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        this.isPrescriptionRequired = product.getIsPrescriptionRequired();
    }
}
