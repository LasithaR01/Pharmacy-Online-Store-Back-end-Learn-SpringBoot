package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.Stock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    // Find all stock entries for a specific product
    List<Stock> findByProductId(Integer productId);

    // Find all stock entries for a specific supplier
    List<Stock> findBySupplierId(Integer supplierId);

    // Find all stock entries for a specific branch
    List<Stock> findByBranchId(Integer branchId);

    // Find all stock entries that are expired
    @Query("SELECT s FROM Stock s WHERE s.expiryDate < CURRENT_DATE")
    List<Stock> findExpiredStock();

    // Find all stock entries that will expire soon (within X days)
    @Query("SELECT s FROM Stock s WHERE s.expiryDate BETWEEN CURRENT_DATE AND :thresholdDate")
    List<Stock> findStockExpiringSoon(LocalDate thresholdDate);

    // Find stock entries by batch number
    List<Stock> findByBatchNumber(String batchNumber);

    // Find stock entries approved by a specific user
    List<Stock> findByApprovedById(Integer userId);

    // Find stock entries added after a specific date
    List<Stock> findByDateAddedAfter(LocalDateTime date);

    // Find stock entries with quantity greater than specified
    List<Stock> findByQuantityAddedGreaterThan(Integer quantity);

    // Custom query to find stock entries with total cost above a certain value
    @Query("SELECT s FROM Stock s WHERE (s.quantityAdded * s.unitCost) > :minTotalCost")
    List<Stock> findByTotalCostGreaterThan(Double minTotalCost);

    // Find stock entries by product and branch
    Stock findByProductIdAndBranchId(Integer productId, Integer branchId);

    // Find stock entries that haven't expired yet
    @Query("SELECT s FROM Stock s WHERE s.expiryDate IS NULL OR s.expiryDate >= CURRENT_DATE")
    List<Stock> findNonExpiredStock();
}