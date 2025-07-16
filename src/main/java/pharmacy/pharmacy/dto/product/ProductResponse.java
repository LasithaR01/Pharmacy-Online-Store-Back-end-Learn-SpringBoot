package pharmacy.pharmacy.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String categoryName;
    private String barcode;
    private Integer stockQuantity;
    private LocalDate expiryDate;
}
