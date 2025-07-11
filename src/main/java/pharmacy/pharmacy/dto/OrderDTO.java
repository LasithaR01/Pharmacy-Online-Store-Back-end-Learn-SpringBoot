package pharmacy.pharmacy.dto;

import lombok.Data;
import pharmacy.pharmacy.enums.OrderStatus;
import pharmacy.pharmacy.enums.PaymentMethod;
import pharmacy.pharmacy.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Integer id;
    private Integer userId;
    private Integer branchId;
    private Date orderDate;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String notes;
    private Integer processedById;
    private List<OrderItemDTO> orderItems;

    // Additional fields for display purposes
    private String userName;
    private String branchName;
    private String processedByName;
    private String customerName;
}