package pharmacy.pharmacy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Data Transfer Object for Category operations")
public class CategoryDTO {

    @Schema(description = "Unique identifier of the category", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Name of the category", example = "Pain Relief", required = true)
    private String name;

    @Schema(description = "Detailed description of the category", example = "Medications for pain management")
    private String description;

    @Schema(description = "ID of the parent category (for hierarchical structure)",
             example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID parentCategoryId;

    @Schema(description = "Name of the parent category (read-only)",
            example = "Medications",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String parentCategoryName;

    // Constructors
    public CategoryDTO() {
    }

    public CategoryDTO(UUID id, String name, String description, UUID parentCategoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(UUID parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    // Utility methods
    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                ", parentCategoryName='" + parentCategoryName + '\'' +
                '}';
    }
}