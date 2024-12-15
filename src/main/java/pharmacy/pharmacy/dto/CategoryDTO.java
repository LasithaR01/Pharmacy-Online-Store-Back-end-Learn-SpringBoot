package pharmacy.pharmacy.dto;

import pharmacy.pharmacy.entity.Category;


import java.util.UUID;

public class CategoryDTO {
    private UUID id;
    private String name;
    private String categoryName;

    // Constructor to map Product to DTO
    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
      //  this.categoryName = category.getCategory() != null ? category.getCategory().getName() : null;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
