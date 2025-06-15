package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pharmacy.pharmacy.entity.Branch;
import pharmacy.pharmacy.entity.Product;
import java.util.Date;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inventory_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inventory_branch"))
    private Branch branch;

    @Column(name = "shelf_location", length = 50)
    private String shelfLocation;

    @Column(name = "stock_level", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer stockLevel = 0;

    @Column(name = "minimum_stock_level")
    private Integer minimumStockLevel;

    @Column(name = "maximum_stock_level")
    private Integer maximumStockLevel;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_restocked")
    private Date lastRestocked;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated;

    @Column(name = "expiry_alert", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean expiryAlert = false;

    @Column(name = "low_stock_alert", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean lowStockAlert = false;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    // Business logic methods
    public boolean needsRestocking() {
        return minimumStockLevel != null && stockLevel <= minimumStockLevel;
    }

    public boolean isOverstocked() {
        return maximumStockLevel != null && stockLevel > maximumStockLevel;
    }

    public void updateStockLevel(int quantity) {
        this.stockLevel += quantity;
        if (quantity > 0) {
            this.lastRestocked = new Date();
        }
    }
}