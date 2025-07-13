package pharmacy.pharmacy.dto.stock;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class StockCreateRequest {
    @NotNull(message = "Product is required")
    private Integer productId;

    @NotNull(message = "Supplier is required")
    private Integer supplierId;

    @NotNull(message = "Quantity is required")
    private Integer quantityAdded;

    @NotNull(message = "Unit cost is required")
    private Double unitCost;

    @NotNull(message = "Expiry Date is required")
    private Date expiryDate;

    @NotNull(message = "BatchNumber is required")
    private String batchNumber;

    @NotNull(message = "Branch is required")
    private Integer branchId;

    private Integer approvedById;

}
