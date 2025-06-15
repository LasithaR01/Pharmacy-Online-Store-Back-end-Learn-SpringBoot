package pharmacy.pharmacy.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountAmount;

    // Additional fields for display purposes
    private String productName;
    private String productBarcode;
    private BigDecimal totalPrice;
}