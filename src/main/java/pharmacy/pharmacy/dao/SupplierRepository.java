package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.Supplier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    // Find by company name (exact match)
    Optional<Supplier> findByCompanyName(String companyName);

    // Find by company name containing (case-insensitive)
    List<Supplier> findByCompanyNameContainingIgnoreCase(String companyName);

    // Find suppliers with active stock
    @Query("SELECT DISTINCT s FROM Supplier s JOIN s.stockEntries st WHERE st.quantityAdded > 0")
    List<Supplier> findSuppliersWithActiveStock();

    // Find by tax ID
    Optional<Supplier> findByTaxId(String taxId);

    // Find suppliers with no associated user
    List<Supplier> findByUserIsNull();

    // Find by user ID
    Optional<Supplier> findByUserId(Integer userId);

    // Count total suppliers
    long count();

    // Find suppliers created after a certain date
    List<Supplier> findByCreatedAtAfter(LocalDateTime date);

    // Custom query for supplier statistics
    @Query("SELECT s.companyName, COUNT(st), SUM(st.quantityAdded) " +
           "FROM Supplier s LEFT JOIN s.stockEntries st " +
           "GROUP BY s.id")
    List<Object[]> getSupplierStatistics();
}