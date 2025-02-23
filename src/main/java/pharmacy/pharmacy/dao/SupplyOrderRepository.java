package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.pharmacy.entity.SupplyOrder;

import java.util.UUID;

public interface SupplyOrderRepository extends JpaRepository<SupplyOrder, UUID> {
}
