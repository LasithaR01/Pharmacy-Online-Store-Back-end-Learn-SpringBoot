package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pharmacy.pharmacy.enums.PrescriptionStatus;

import java.util.Date;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_prescription_user"))
    private User user;

    @Column(name = "doctor_name", nullable = false, length = 255)
    private String doctorName;

    @Column(name = "doctor_contact", nullable = false, length = 20)
    private String doctorContact;

    @Column(name = "prescription_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date prescriptionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status = PrescriptionStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "document_url", length = 255)
    private String documentUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", foreignKey = @ForeignKey(name = "fk_prescription_approved_by"))
    private User approvedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "approved_at")
    private Date approvedAt;

    // Business logic methods
    public boolean canBeApproved() {
        return this.status == PrescriptionStatus.PENDING;
    }

    public boolean canBeRejected() {
        return this.status == PrescriptionStatus.PENDING;
    }

    public void approve(User approvedByUser) {
        if (!canBeApproved()) {
            throw new IllegalStateException("Prescription cannot be approved in current status");
        }
        this.status = PrescriptionStatus.APPROVED;
        this.approvedBy = approvedByUser;
        this.approvedAt = new Date();
    }

    public void reject(User rejectedByUser) {
        if (!canBeRejected()) {
            throw new IllegalStateException("Prescription cannot be rejected in current status");
        }
        this.status = PrescriptionStatus.REJECTED;
        this.approvedBy = rejectedByUser;
        this.approvedAt = new Date();
    }
}