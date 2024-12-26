package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.pharmacy.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    // Check if a product with a specific slug exists
    boolean existsBySlug(String slug);

    // Find a product by its slug
    Optional<Product> findBySlug(String slug);

}
