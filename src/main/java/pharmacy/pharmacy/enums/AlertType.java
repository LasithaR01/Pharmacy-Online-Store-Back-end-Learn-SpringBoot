package pharmacy.pharmacy.enums;

public enum AlertType {
    LOW_STOCK,          // When stock falls below minimum level
    OUT_OF_STOCK,       // When product is completely out of stock
    EXPIRY_WARNING,     // When product is nearing expiry (30-60 days)
    EXPIRY_CRITICAL,    // When product is very close to expiry (<30 days)
    REORDER,            // When automatic reorder is suggested
    TRANSFER_REQUEST,   // When inventory transfer is needed
    SYSTEM_ALERT,       // For system-level alerts
    DATA_ISSUE          // For data inconsistencies
}