package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.pharmacy.entity.Order;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.enums.OrderStatus;
import pharmacy.pharmacy.enums.PaymentMethod;
import pharmacy.pharmacy.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Find orders by user
    List<Order> findByUser(User user);

    // Find orders by user ID
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findByUserId(@Param("userId") Integer userId);

    // Find orders by branch
    List<Order> findByBranchId(Integer branchId);

    // Find orders by status
    List<Order> findByStatus(OrderStatus status);

    // Find orders by payment status
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);

    // Find orders by date range
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Find orders by user and status
    List<Order> findByUserAndStatus(User user, OrderStatus status);

    // Find orders by user ID and status
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = :status")
    List<Order> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") OrderStatus status);

    // Find orders with total amount greater than
    List<Order> findByTotalAmountGreaterThan(BigDecimal amount);

    // Find orders with total amount less than
    List<Order> findByTotalAmountLessThan(BigDecimal amount);

    // Find orders by payment method
    List<Order> findByPaymentMethod(PaymentMethod paymentMethod);

    // Check if user has any orders
    boolean existsByUser(User user);

    // Check if user ID has any orders
    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.user.id = :userId")
    boolean existsByUserId(@Param("userId") Integer userId);

    // Count orders by status
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@Param("status") OrderStatus status);

    // Count orders by payment status
    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :paymentStatus")
    long countByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);

    // Find orders processed by a specific staff member
    List<Order> findByProcessedBy(User processedBy);

    // Find orders processed by staff member ID
    @Query("SELECT o FROM Order o WHERE o.processedBy.id = :staffId")
    List<Order> findByProcessedById(@Param("staffId") Integer staffId);

    // Find the most recent orders (limit by count)
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC LIMIT :count")
    List<Order> findRecentOrders(@Param("count") int count);

    // Calculate total sales for a period
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.status = 'COMPLETED'")
    Optional<BigDecimal> calculateTotalSalesBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}