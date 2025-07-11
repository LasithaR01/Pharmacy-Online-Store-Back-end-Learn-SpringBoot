package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.pharmacy.entity.*;
import pharmacy.pharmacy.enums.RestockStatus;

import java.util.Date;
import java.util.List;

public interface RestockRequestRepository extends JpaRepository<RestockRequest, Integer> {

    // Find requests by product
    List<RestockRequest> findByProduct(Product product);

    // Find requests by branch
    List<RestockRequest> findByBranch(Branch branch);

    // Find requests by status
    List<RestockRequest> findByStatus(RestockStatus status);

    // Find requests by supplier
    List<RestockRequest> findBySupplier(Supplier supplier);

    // Find requests by requester
    List<RestockRequest> findByRequestedBy(User requestedBy);

    // Find requests by approver
    List<RestockRequest> findByApprovedBy(User approvedBy);

    // Find pending requests created before certain date
    @Query("SELECT r FROM RestockRequest r WHERE r.status = 'PENDING' AND r.createdAt < :date")
    List<RestockRequest> findPendingOlderThan(@Param("date") Date date);

    // Approve multiple requests
    @Modifying
    @Query("UPDATE RestockRequest r SET r.status = 'APPROVED', r.approvedBy = :approvedBy, r.approvedAt = CURRENT_TIMESTAMP WHERE r.id IN :ids")
    void approveRequests(@Param("ids") List<Integer> requestIds, @Param("approvedBy") User approvedBy);

    // Count pending requests
    long countByStatus(RestockStatus status);

    // Find requests needing attention (pending or approved but not fulfilled)
    @Query("SELECT r FROM RestockRequest r WHERE r.status IN ('PENDING', 'APPROVED')")
    List<RestockRequest> findActiveRequests();
}