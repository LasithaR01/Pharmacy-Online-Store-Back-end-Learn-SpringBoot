package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.pharmacy.entity.Prescription;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.enums.PrescriptionStatus;

import java.util.Date;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

    // Find prescriptions by user with pagination
    List<Prescription> findByUser(User user);

    // Find prescriptions by user ID
    @Query("SELECT p FROM Prescription p WHERE p.user.id = :userId ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByUserId(@Param("userId") Integer userId);

    // Find prescriptions by status
    List<Prescription> findByStatus(PrescriptionStatus status);

    // Find prescriptions by doctor name (case-insensitive search)
    @Query("SELECT p FROM Prescription p WHERE LOWER(p.doctorName) LIKE LOWER(concat('%', :doctorName, '%'))")
    List<Prescription> findByDoctorNameContaining(@Param("doctorName") String doctorName);

    // Find prescriptions within date range
    @Query("SELECT p FROM Prescription p WHERE p.prescriptionDate BETWEEN :startDate AND :endDate")
    List<Prescription> findByPrescriptionDateBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    // Find expiring prescriptions (approved but older than 6 months)
    @Query("SELECT p FROM Prescription p WHERE p.status = 'APPROVED' AND p.prescriptionDate < :expiryDate")
    List<Prescription> findExpiringPrescriptions(@Param("expiryDate") Date expiryDate);

    // Count prescriptions by status
    long countByStatus(PrescriptionStatus status);

    // Check if user has any prescriptions
    boolean existsByUser(User user);

    // Find prescriptions by status and user
    List<Prescription> findByStatusAndUser(PrescriptionStatus status, User user);

    // Find prescriptions that need review (pending or expiring soon)
    @Query("SELECT p FROM Prescription p WHERE p.status = 'PENDING' OR (p.status = 'APPROVED' AND p.prescriptionDate < :warningDate)")
    List<Prescription> findPrescriptionsNeedingReview(@Param("warningDate") Date warningDate);
}