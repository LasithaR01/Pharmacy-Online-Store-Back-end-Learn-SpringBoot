package pharmacy.pharmacy.enums;

public enum EmployeePosition {
    ADMIN("Admin"),
    PHARMACIST("Pharmacist"),
    CASHIER("Cashier"),
    MANAGER("Manager"),
    TECHNICIAN("Pharmacy Technician"),
    INVENTORY_MANAGER("Inventory Manager"),
    CUSTOMER_SERVICE("Customer Service");

    private final String displayName;

    EmployeePosition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Helper method to convert from string to enum
    public static EmployeePosition fromString(String text) {
        for (EmployeePosition position : EmployeePosition.values()) {
            if (position.name().equalsIgnoreCase(text)) {
                return position;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    // Check if this is a management position
    public boolean isManagement() {
        return this == ADMIN || this == MANAGER || this == INVENTORY_MANAGER;
    }

    // Check if this position can handle prescriptions
    public boolean canHandlePrescriptions() {
        return this == PHARMACIST || this == ADMIN || this == MANAGER;
    }

    // Check if this position can process payments
    public boolean canProcessPayments() {
        return this == CASHIER || this == PHARMACIST || this == ADMIN || this == MANAGER;
    }
}