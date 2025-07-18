package pharmacy.pharmacy.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductDTO {
    private String name;
    private Integer categoryId;
    private String description;
    private String imageUrl;  // Added image URL field
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private String batchNumber;
    private String barcode;
    private Boolean isPrescriptionRequired;
}