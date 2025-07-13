package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pharmacy.pharmacy.enums.RestockStatus;

import java.util.Date;

@Entity
@Table(name = "restock_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_restock_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false, foreignKey = @ForeignKey(name = "fk_restock_branch"))
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requested_by", nullable = false, foreignKey = @ForeignKey(name = "fk_restock_requested_by"))
    private User requestedBy;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestockStatus status = RestockStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", foreignKey = @ForeignKey(name = "fk_restock_supplier"))
    private Supplier supplier;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", foreignKey = @ForeignKey(name = "fk_restock_approved_by"))
    private User approvedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "approved_at")
    private Date approvedAt;

    // Business logic methods
    public boolean canBeApproved() {
        return this.status == RestockStatus.PENDING;
    }

    public boolean canBeRejected() {
        return this.status == RestockStatus.PENDING;
    }

    public boolean canBeFulfilled() {
        return this.status == RestockStatus.APPROVED;
    }

    public void approve(User approver) {
        if (!canBeApproved()) {
            throw new IllegalStateException("Restock request cannot be approved in current status");
        }
        this.status = RestockStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = new Date();
    }

    public void reject(User rejector) {
        if (!canBeRejected()) {
            throw new IllegalStateException("Restock request cannot be rejected in current status");
        }
        this.status = RestockStatus.REJECTED;
        this.approvedBy = rejector;
        this.approvedAt = new Date();
    }

    public void fulfill() {
        if (!canBeFulfilled()) {
            throw new IllegalStateException("Restock request cannot be fulfilled in current status");
        }
        this.status = RestockStatus.FULFILLED;
    }
}