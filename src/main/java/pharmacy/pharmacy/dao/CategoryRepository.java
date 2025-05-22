package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Find category by name (exact match)
    Optional<Category> findByName(String name);

    // Check if category with name exists
    boolean existsByName(String name);

    // Find all top-level categories (where parent is null)
    List<Category> findByParentCategoryIsNull();

    // Find all child categories of a specific parent
    List<Category> findByParentCategoryId(UUID parentId);

    // Find categories by name containing (case-insensitive)
    List<Category> findByNameContainingIgnoreCase(String name);

    // Custom query to count products in a category
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    long countProductsInCategory(UUID categoryId);

    // Custom query to check if category can be deleted (has no products or subcategories)
    @Query("SELECT CASE WHEN (COUNT(p) = 0 AND (SELECT COUNT(c) FROM Category c WHERE c.parentCategory.id = :categoryId) = 0) THEN true ELSE false END FROM Product p WHERE p.category.id = :categoryId")
    boolean isCategoryDeletable(UUID categoryId);
}