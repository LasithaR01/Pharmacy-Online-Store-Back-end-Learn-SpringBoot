package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import io.sentry.Sentry;
import java.util.UUID;

@Entity
@Table(name = "category")
@Schema(description = "Entity representing a product category in the pharmacy system")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the category", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    @Schema(description = "Name of the category", example = "Pain Relief", required = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Detailed description of the category", example = "Medications for pain management")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @Schema(description = "Parent category reference for hierarchical structure")
    private Category parentCategory;

    public Category() {
        try {
            // Initialize with default values if needed
        } catch (Exception e) {
            Sentry.captureException(e);
            throw e;
        }
    }

    public Category(UUID id, String name, String description, Category parentCategory) {
        try {
            this.id = id;
            this.name = name;
            this.description = description;
            this.parentCategory = parentCategory;
        } catch (Exception e) {
            Sentry.captureException(e);
            throw e;
        }
    }

    // Getters and Setters with Sentry error tracking
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        try {
            this.id = id;
        } catch (Exception e) {
            Sentry.captureException(e);
            throw e;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        try {
            this.name = name;
        } catch (Exception e) {
            Sentry.captureException(e);
            throw e;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        try {
            this.description = description;
        } catch (Exception e) {
            Sentry.captureException(e);
            throw e;
        }
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        try {
            this.parentCategory = parentCategory;
        } catch (Exception e) {
            Sentry.captureException(e);
            throw e;
        }
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parentCategory=" + (parentCategory != null ? parentCategory.getName() : "null") +
                '}';
    }
}