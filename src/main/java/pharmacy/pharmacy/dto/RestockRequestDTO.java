package pharmacy.pharmacy.dto;

import lombok.Data;
import pharmacy.pharmacy.enums.RestockStatus;

import java.util.Date;

@Data
public class RestockRequestDTO {
    private Integer id;
    private Integer productId;
    private Integer branchId;
    private Integer requestedById;
    private Integer quantity;
    private RestockStatus status;
    private Integer supplierId;
    private String notes;
    private Integer approvedById;
    private Date createdAt;
    private Date approvedAt;

    // Additional display fields
    private String productName;
    private String branchName;
    private String requestedByName;
    private String supplierName;
    private String approvedByName;
}