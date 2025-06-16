package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.pharmacy.entity.DrugInteraction;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.enums.InteractionSeverity;

import java.util.List;

public interface DrugInteractionRepository extends JpaRepository<DrugInteraction, Integer> {

    // Find interactions involving a specific product (either as product or interactsWith)
    @Query("SELECT di FROM DrugInteraction di WHERE di.product.id = :productId OR di.interactsWith.id = :productId")
    List<DrugInteraction> findByProductInvolved(@Param("productId") int productId);

    // Find interactions between two specific products
    @Query("SELECT di FROM DrugInteraction di WHERE " +
           "(di.product.id = :productId1 AND di.interactsWith.id = :productId2) OR " +
           "(di.product.id = :productId2 AND di.interactsWith.id = :productId1)")
    List<DrugInteraction> findInteractionBetweenProducts(
            @Param("productId1") int productId1,
            @Param("productId2") int productId2);

    // Find interactions by severity level
    List<DrugInteraction> findBySeverity(InteractionSeverity severity);

    // Check if interaction exists between two products
    @Query("SELECT COUNT(di) > 0 FROM DrugInteraction di WHERE " +
           "(di.product.id = :productId1 AND di.interactsWith.id = :productId2) OR " +
           "(di.product.id = :productId2 AND di.interactsWith.id = :productId1)")
    boolean existsInteractionBetweenProducts(
            @Param("productId1") int productId1,
            @Param("productId2") int productId2);
}