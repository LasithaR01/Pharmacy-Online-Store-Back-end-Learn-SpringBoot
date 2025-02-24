package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.Stock;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    // Custom query to find stocks by product ID
    List<Stock> findByProductId(UUID productId);

    // Custom query to find stocks by supplier ID
    List<Stock> findBySupplierId(UUID supplierId);
}
