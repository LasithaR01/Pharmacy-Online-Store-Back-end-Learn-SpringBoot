package pharmacy.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pharmacy.pharmacy.entity.OrderItem;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.orderId = orderItem.getOrder().getId();
        this.productId = orderItem.getProduct().getId();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
        this.discountAmount = orderItem.getDiscountAmount();
        this.productName = orderItem.getProduct().getName();
        this.productBarcode = orderItem.getProduct().getBarcode();
        this.totalPrice = orderItem.getTotalPrice();
    }
}