package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "supply_order")
public class SupplyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

//    @ManyToOne
//    @JoinColumn(name = "branch_id", nullable = false)
//    private Branch branch;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date orderDate;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private OrderStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Column(nullable = true, unique = true)
    private String slug;

    // PrePersist lifecycle callback to set createdAt and generate slug
    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        generateSlug();
    }

    // PreUpdate lifecycle callback to generate slug
    @PreUpdate
    public void preUpdate() {
        generateSlug();
    }

    // Method to generate slug from supplier name and order date
    private void generateSlug() {
        if (supplier != null && orderDate != null) {
            this.slug = supplier.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-$", "")
                    + "-" + orderDate.getTime();
        }
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

//    public Branch getBranch() {
//        return branch;
//    }
//
//    public void setBranch(Branch branch) {
//        this.branch = branch;
//    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

//    public OrderStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(OrderStatus status) {
//        this.status = status;
//    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
