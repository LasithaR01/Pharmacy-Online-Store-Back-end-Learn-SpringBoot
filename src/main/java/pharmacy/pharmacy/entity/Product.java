package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically generates UUIDs
//    @Column(columnDefinition = "UUID") // Ensures the database treats it as a UUID
    private UUID id;

    @Column(nullable = false) // Ensure 'name' is not null in the database
    private String name; // This will store the original product name

    @Column(nullable = false, unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY) // Defines the relationship
    @JoinColumn(name = "category_id", nullable = true) // Foreign key column
    private Category category;

    private String description;

    private double price;

    private int stockQuantity;

    private Date expiryDate;

    private int batchNumber;

    // Pre-persist and pre-update lifecycle hooks to generate slug from name
    @PrePersist
    @PreUpdate
    public void generateSlug() {
        if (name != null) {
            // Convert the name to a URL-friendly slug
            this.slug = name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-$", "");
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }
}
