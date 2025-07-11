package pharmacy.pharmacy.enums;

public enum RestockStatus {
    PENDING,    // Waiting for approval
    APPROVED,   // Approved by manager
    REJECTED,   // Rejected by manager
    FULFILLED,  // Completed by supplier
    CANCELLED   // Cancelled by requester
}