package pharmacy.pharmacy.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class SupplyOrderRequestDTO {
    private UUID supplierId;
    private UUID branchId;
    private Date orderDate;
    private String status;

    public Date getOrderDate() {
        return null;
    }
}
