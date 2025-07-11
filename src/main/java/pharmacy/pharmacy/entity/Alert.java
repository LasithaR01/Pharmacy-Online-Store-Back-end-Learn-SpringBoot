package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pharmacy.pharmacy.enums.AlertType;
import pharmacy.pharmacy.enums.AlertStatus;

import java.util.Date;

@Entity
@Table(name = "alert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_alert_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", foreignKey = @ForeignKey(name = "fk_alert_branch"))
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by", foreignKey = @ForeignKey(name = "fk_alert_triggered_by"))
    private User triggeredBy;

    @Column(nullable = false)
    private boolean resolved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by", foreignKey = @ForeignKey(name = "fk_alert_resolved_by"))
    private User resolvedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "resolved_at")
    private Date resolvedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AlertStatus status = AlertStatus.ACTIVE;

    // Business logic methods
    public void resolve(User resolver) {
        if (this.resolved) {
            throw new IllegalStateException("Alert is already resolved");
        }
        this.resolved = true;
        this.resolvedBy = resolver;
        this.resolvedAt = new Date();
        this.status = AlertStatus.RESOLVED;
    }

    public void reopen() {
        if (!this.resolved) {
            throw new IllegalStateException("Alert is not resolved");
        }
        this.resolved = false;
        this.resolvedBy = null;
        this.resolvedAt = null;
        this.status = AlertStatus.REOPENED;
    }

    public boolean isCritical() {
        return this.alertType == AlertType.OUT_OF_STOCK ||
               this.alertType == AlertType.EXPIRY_CRITICAL;
    }

    public boolean requiresImmediateAttention() {
        return isCritical() ||
               this.alertType == AlertType.LOW_STOCK ||
               this.alertType == AlertType.TRANSFER_REQUEST;
    }
}