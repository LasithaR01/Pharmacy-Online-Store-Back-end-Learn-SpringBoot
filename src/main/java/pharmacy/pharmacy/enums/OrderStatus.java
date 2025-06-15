package pharmacy.pharmacy.enums;

public enum OrderStatus {
    CART,          // Order is still in shopping cart
    PENDING,       // Order submitted but not processed
    PROCESSING,    // Order is being prepared
    READY,        // Order is ready for pickup/delivery
    SHIPPED,      // Order has been shipped (for deliveries)
    DELIVERED,    // Order has been delivered
    COMPLETED,    // Order is completed (picked up or delivered)
    CANCELLED,    // Order was cancelled
    REFUNDED,     // Order was refunded
    FAILED        // Order processing failed
}