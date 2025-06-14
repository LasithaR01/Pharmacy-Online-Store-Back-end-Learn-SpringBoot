package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.pharmacy.entity.Order;
import pharmacy.pharmacy.entity.OrderItem;
import pharmacy.pharmacy.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    // Find all items for an order
    List<OrderItem> findByOrder(Order order);

    // Find all items for an order ID
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Integer orderId);

    // Find all items for a product
    List<OrderItem> findByProduct(Product product);

    // Find all items for a product ID
    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.id = :productId")
    List<OrderItem> findByProductId(@Param("productId") Integer productId);

    // Find specific order item by order and product
    OrderItem findByOrderAndProduct(Order order, Product product);

    // Find specific order item by order ID and product ID
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.product.id = :productId")
    Optional<OrderItem> findByOrderIdAndProductId(@Param("orderId") Integer orderId, @Param("productId") Integer productId);

    // Count how many times a product has been ordered
    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.product.id = :productId")
    long countByProductId(@Param("productId") Integer productId);

    // Calculate total quantity sold for a product
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId")
    Optional<Integer> sumQuantityByProductId(@Param("productId") Integer productId);

    // Calculate total revenue for a product
    @Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId")
    Optional<BigDecimal> calculateTotalRevenueByProductId(@Param("productId") Integer productId);

    // Delete all items for an order
    void deleteByOrder(Order order);

    // Delete all items for an order ID
    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Integer orderId);

    // Check if product exists in any order
    boolean existsByProduct(Product product);

    // Check if product ID exists in any order
    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.product.id = :productId")
    boolean existsByProductId(@Param("productId") Integer productId);

    // Find top selling products (limit by count)
    @Query("SELECT oi.product.id, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi GROUP BY oi.product.id ORDER BY totalQuantity DESC LIMIT :count")
    List<Object[]> findTopSellingProducts(@Param("count") int count);

    // Find order items for completed orders
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.status = 'COMPLETED'")
    List<OrderItem> findCompletedOrderItems();

    int countByOrder(Order order);
}