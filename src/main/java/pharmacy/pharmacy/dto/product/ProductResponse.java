package pharmacy.pharmacy.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String categoryName;
    private String barcode;
    private Integer stockQuantity;
}
