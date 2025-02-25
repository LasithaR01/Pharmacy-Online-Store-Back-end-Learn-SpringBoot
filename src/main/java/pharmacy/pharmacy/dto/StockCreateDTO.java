package pharmacy.pharmacy.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StockCreateDTO {

    private UUID productId;
    private UUID supplierId;
    private int quantityAdded;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public int getQuantityAdded() {
        return quantityAdded;
    }

    public void setQuantityAdded(int quantityAdded) {
        this.quantityAdded = quantityAdded;
    }
}
