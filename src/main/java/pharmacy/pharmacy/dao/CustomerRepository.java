package pharmacy.pharmacy.dao;

import pharmacy.pharmacy.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // Find customer by user ID
    Optional<Customer> findByUserId(Integer userId);

    // Check if user is already a customer
    boolean existsByUserId(Integer userId);

    // Find customers by loyalty points range
    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints BETWEEN :minPoints AND :maxPoints")
    List<Customer> findByLoyaltyPointsRange(@Param("minPoints") Integer minPoints,
                                           @Param("maxPoints") Integer maxPoints);

    // Find customers born after a specific date
    List<Customer> findByDateOfBirthAfter(Date dateOfBirth);

    // Find customers by address containing text
    List<Customer> findByAddressContaining(String addressPart);
}