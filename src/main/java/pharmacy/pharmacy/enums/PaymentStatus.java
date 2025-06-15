package pharmacy.pharmacy.enums;

public enum PaymentStatus {
    PENDING,      // Payment not yet processed
    AUTHORIZED,   // Payment authorized but not captured
    PAID,         // Payment successfully completed
    FAILED,       // Payment failed
    REFUNDED,     // Payment was refunded
    PARTIALLY_REFUNDED,  // Partial refund was issued
    CANCELLED,    // Payment was cancelled
    EXPIRED,      // Payment authorization expired
    DECLINED      // Payment was declined
}