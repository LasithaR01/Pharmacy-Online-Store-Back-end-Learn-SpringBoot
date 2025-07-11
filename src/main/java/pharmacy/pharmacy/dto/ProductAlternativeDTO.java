package pharmacy.pharmacy.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProductAlternativeDTO {
    private Integer id;
    private Integer productId;
    private Integer alternativeProductId;
    private String reason;
    private Date createdAt;

    // Additional display fields
    private String productName;
    private String alternativeProductName;
    private String productCategory;
    private String alternativeProductCategory;
    private boolean sameCategory;
    private boolean cheaperAlternative;
    private boolean inStock;
}