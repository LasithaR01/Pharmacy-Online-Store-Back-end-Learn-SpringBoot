package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.pharmacy.entity.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Check if a category with a specific slug exists
    boolean existsBySlug(String slug);

    // Find a category by its slug
    Optional<Category> findBySlug(String slug);
}
