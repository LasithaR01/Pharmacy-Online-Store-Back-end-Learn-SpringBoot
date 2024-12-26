package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.pharmacy.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findBySlug(String slug);
}
