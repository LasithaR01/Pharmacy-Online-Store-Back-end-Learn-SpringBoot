package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "stock")
@Getter
@Setter
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "quantity_added", nullable = false)
    private Integer quantityAdded;

    @Column(name = "unit_cost", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double unitCost;

    @CreationTimestamp
    @Column(name = "date_added", updatable = false)
    private LocalDateTime dateAdded;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    // Constructors
    public Stock() {}

    public Stock(Product product, Supplier supplier, Integer quantityAdded, Double unitCost) {
        this.product = product;
        this.supplier = supplier;
        this.quantityAdded = quantityAdded;
        this.unitCost = unitCost;
    }

    // Business methods
    public Double getTotalCost() {
        return quantityAdded * unitCost;
    }

    public boolean isExpired() {
        if (expiryDate == null) return false;
        return expiryDate.before(new Date());
    }
}