package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.Product;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByBarcode(String barcode);

    Optional<Product> findByBarcode(String barcode);

    List<Product> findByCategoryId(Integer categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByIsPrescriptionRequired(Boolean isPrescriptionRequired);

    List<Product> findByStockQuantityLessThanEqual(Integer stockQuantity);

    List<Product> findByExpiryDateBefore(LocalDate date);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.reorderLevel AND p.reorderLevel > 0")
    List<Product> findProductsBelowReorderLevel();

    @Query("SELECT p FROM Product p WHERE p.expiryDate BETWEEN CURRENT_DATE AND :thresholdDate")
    List<Product> findProductsExpiringSoon(LocalDate thresholdDate);

    @Query("SELECT p FROM Product p JOIN p.inventories i WHERE i.branch.id = :branchId AND i.stockLevel <= :threshold")
    List<Product> findLowStockProductsByBranch(Integer branchId, Integer threshold);
}