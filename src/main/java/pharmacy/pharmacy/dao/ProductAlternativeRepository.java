package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.pharmacy.entity.ProductAlternative;

import java.util.List;

public interface ProductAlternativeRepository extends JpaRepository<ProductAlternative, Integer> {

    // Find all alternatives for a specific product
    List<ProductAlternative> findByProductId(int productId);

    // Find all alternatives where a product is listed as alternative
    List<ProductAlternative> findByAlternativeProductId(int alternativeProductId);

    // Check if alternative relationship already exists
    boolean existsByProductIdAndAlternativeProductId(int productId, int alternativeProductId);

    // Find alternatives with stock level above threshold
    @Query("SELECT pa FROM ProductAlternative pa WHERE pa.alternativeProduct.stockQuantity > :minStock")
    List<ProductAlternative> findAlternativesWithStockAbove(@Param("minStock") int minStock);

    // Find cheaper alternatives (price less than original product)
    @Query("SELECT pa FROM ProductAlternative pa WHERE pa.alternativeProduct.price < pa.product.price")
    List<ProductAlternative> findCheaperAlternatives();

    // Find alternatives in same category
    @Query("SELECT pa FROM ProductAlternative pa WHERE pa.product.category = pa.alternativeProduct.category")
    List<ProductAlternative> findSameCategoryAlternatives();
}