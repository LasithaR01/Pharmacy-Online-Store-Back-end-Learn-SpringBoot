package pharmacy.pharmacy.enums;

public enum AlertStatus {
    ACTIVE,     // New/unresolved alert
    RESOLVED,   // Successfully resolved
    REOPENED,   // Was resolved but reopened
    IGNORED,    // Manually ignored
    EXPIRED     // Automatically expired (e.g., after product restock)
}