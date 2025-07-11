package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.pharmacy.entity.Alert;
import pharmacy.pharmacy.entity.Branch;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.enums.AlertStatus;
import pharmacy.pharmacy.enums.AlertType;

import java.util.Date;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Integer> {

    // Find alerts by product
    List<Alert> findByProduct(Product product);

    // Find alerts by branch
    List<Alert> findByBranch(Branch branch);

    // Find unresolved alerts
    List<Alert> findByResolvedFalse();

    // Find resolved alerts
    List<Alert> findByResolvedTrue();

    // Find alerts by status
    List<Alert> findByStatus(AlertStatus status);

    // Find alerts by type
    List<Alert> findByAlertType(AlertType alertType);

    // Find critical alerts (OUT_OF_STOCK or EXPIRY_CRITICAL)
    @Query("SELECT a FROM Alert a WHERE a.alertType IN ('OUT_OF_STOCK', 'EXPIRY_CRITICAL')")
    List<Alert> findCriticalAlerts();

    // Find alerts created after certain date
    List<Alert> findByCreatedAtAfter(Date date);

    // Find alerts by triggered user
    List<Alert> findByTriggeredBy(User user);

    // Resolve multiple alerts
    @Modifying
    @Query("UPDATE Alert a SET a.resolved = true, a.resolvedBy = :resolvedBy, a.resolvedAt = CURRENT_TIMESTAMP, a.status = 'RESOLVED' WHERE a.id IN :ids")
    void resolveAlerts(@Param("ids") List<Integer> alertIds, @Param("resolvedBy") User resolvedBy);

    // Count unresolved alerts
    long countByResolvedFalse();

    // Delete resolved alerts older than specified date
    @Modifying
    @Query("DELETE FROM Alert a WHERE a.resolved = true AND a.resolvedAt < :date")
    void deleteResolvedOlderThan(@Param("date") Date date);
}