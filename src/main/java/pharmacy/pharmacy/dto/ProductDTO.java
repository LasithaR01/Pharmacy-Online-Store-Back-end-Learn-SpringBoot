package pharmacy.pharmacy.dto;

import pharmacy.pharmacy.entity.Product;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class ProductDTO {
    private UUID id;
    private String name;
    private String slug;
    private UUID categoryId;
    private String categoryName;
    private String description;
    private double price;
    private double costPrice;
    private int stockQuantity;
    private Date expiryDate;
    private int batchNumber;
    private int barcode;
    private String isPrescriptionRequired;
    private LocalDateTime createdAt;

    // Constructor to map Product to DTO
//    public ProductDTO(Product product) {
//        this.id = product.getId();
//        this.name = product.getName();
//        this.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
//
//    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.slug = product.getSlug();
        this.categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        this.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.costPrice = product.getCostPrice();
        this.stockQuantity = product.getStockQuantity();
        this.expiryDate = product.getExpiryDate();
        this.batchNumber = product.getBatchNumber();
        this.barcode = product.getBarcode();
        this.isPrescriptionRequired = product.getIsPrescriptionRequired();
        this.createdAt = product.getCreatedAt();
    }


    // Getters and Setters
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

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
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

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getIsPrescriptionRequired() {
        return isPrescriptionRequired;
    }

    public void setIsPrescriptionRequired(String isPrescriptionRequired) {
        this.isPrescriptionRequired = isPrescriptionRequired;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
