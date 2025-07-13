package pharmacy.pharmacy.dto.restockRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pharmacy.pharmacy.enums.RestockStatus;

@Data
public class RestockRequestCreateRequest {
    @NotNull(message = "Product is required")
    private Integer productId;

    @NotNull(message = "Branch is required")
    private Integer branchId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Status is required")
    private RestockStatus status;

    @NotNull(message = "Supplier is required")
    private Integer supplierId;

    private String notes;
}
