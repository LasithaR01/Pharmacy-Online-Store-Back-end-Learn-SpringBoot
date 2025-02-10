package pharmacy.pharmacy.dto;

import pharmacy.pharmacy.entity.Product;

import java.util.UUID;

public class ProductDTO {
    private UUID id;
    private String name;

    private String slug;

    private String categoryName;

    // Constructor to map Product to DTO
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.slug = product.getSlug();
        this.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
