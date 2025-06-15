package pharmacy.pharmacy.enums;

public enum PrescriptionStatus {
    PENDING,    // Waiting for approval
    APPROVED,   // Approved by pharmacist
    REJECTED,   // Rejected by pharmacist
    EXPIRED,    // Prescription is no longer valid
    FULFILLED   // Prescription has been used for an order
}