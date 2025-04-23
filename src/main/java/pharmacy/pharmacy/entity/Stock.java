package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically generates UUIDs
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) // Many stock items can be related to one product
    @JoinColumn(name = "product_id", nullable = false) // Foreign key to Product
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY) // Many stock items can be related to one supplier
    @JoinColumn(name = "supplier_id", nullable = false) // Foreign key to Supplier
    private Supplier supplier;

    @Column(nullable = false)
    private int quantityAdded; // The number of items added to stock

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date dateAdded; // Date the stock was added


    @PrePersist
    public void prePersist() {
        this.dateAdded = new Date();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public int getQuantityAdded() {
        return quantityAdded;
    }

    public void setQuantityAdded(int quantityAdded) {
        this.quantityAdded = quantityAdded;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}

