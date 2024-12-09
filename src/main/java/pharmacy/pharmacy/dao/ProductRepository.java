package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.pharmacy.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
