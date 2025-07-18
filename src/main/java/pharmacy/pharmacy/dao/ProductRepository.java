package pharmacy.pharmacy.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.Product;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Existing product queries
    boolean existsByBarcode(String barcode);
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByIsPrescriptionRequired(Boolean isPrescriptionRequired);
    List<Product> findByStockQuantityLessThanEqual(Integer stockQuantity);
    List<Product> findByExpiryDateBefore(LocalDate date);

    // New image-related queries
    @Query("SELECT p.imageUrl FROM Product p WHERE p.id = :productId")
    Optional<String> findImageUrlById(Integer productId);

    @Query("SELECT p FROM Product p WHERE p.imageUrl IS NOT NULL")
    List<Product> findAllWithImages();

    @Query("SELECT p FROM Product p WHERE p.imageUrl IS NULL")
    List<Product> findAllWithoutImages();

    List<Product> findByImageUrlContaining(String domain); // For finding images from specific domain

    // Existing complex queries
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.reorderLevel AND p.reorderLevel > 0")
    List<Product> findProductsBelowReorderLevel();

    @Query("SELECT p FROM Product p WHERE p.expiryDate BETWEEN CURRENT_DATE AND :thresholdDate")
    List<Product> findProductsExpiringSoon(LocalDate thresholdDate);

    @Query("SELECT p FROM Product p JOIN p.inventories i WHERE i.branch.id = :branchId AND i.stockLevel <= :threshold")
    List<Product> findLowStockProductsByBranch(Integer branchId, Integer threshold);

    // New combined image + stock query
    @Query("SELECT p FROM Product p WHERE p.imageUrl IS NOT NULL AND p.stockQuantity > 0")
    List<Product> findInStockProductsWithImages();

    // New query for image update
    @Query("UPDATE Product p SET p.imageUrl = :imageUrl WHERE p.id = :productId")
    @Modifying
    @Transactional
    int updateProductImage(Integer productId, String imageUrl);
}