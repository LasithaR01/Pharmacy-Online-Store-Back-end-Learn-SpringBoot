package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.Inventory;

import java.util.Date;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    // Find all inventory records for a specific branch
    List<Inventory> findByBranchId(Integer branchId);

    // Find all inventory records for a specific product
    List<Inventory> findByProductId(Integer productId);

    // Find all inventory records with low stock alert
    List<Inventory> findByLowStockAlertTrue();

    // Find inventory records with stock level below minimum threshold
    @Query("SELECT i FROM Inventory i WHERE i.minimumStockLevel IS NOT NULL AND i.stockLevel <= i.minimumStockLevel")
    List<Inventory> findItemsBelowMinimumStock();

    // Find inventory records with stock level above maximum threshold
    @Query("SELECT i FROM Inventory i WHERE i.maximumStockLevel IS NOT NULL AND i.stockLevel > i.maximumStockLevel")
    List<Inventory> findItemsAboveMaximumStock();

    // Find inventory records that need restocking (stock level <= minimum stock level)
    @Query("SELECT i FROM Inventory i WHERE i.minimumStockLevel IS NOT NULL AND i.stockLevel <= i.minimumStockLevel")
    List<Inventory> findItemsNeedingRestock();

    // Find inventory records by branch and product
    Inventory findByBranchIdAndProductId(Integer branchId, Integer productId);

    // Find inventory records with expiry alerts
    List<Inventory> findByExpiryAlertTrue();

    // Custom query to find inventory records with stock level below a certain threshold
    @Query("SELECT i FROM Inventory i WHERE i.stockLevel < :threshold")
    List<Inventory> findByStockLevelBelow(Integer threshold);

    // Custom query to find inventory records that were recently restocked
    @Query("SELECT i FROM Inventory i WHERE i.lastRestocked IS NOT NULL AND i.lastRestocked >= :date")
    List<Inventory> findRecentlyRestocked(Date date);
}