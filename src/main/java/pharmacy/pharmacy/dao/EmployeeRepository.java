package pharmacy.pharmacy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacy.pharmacy.entity.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    // Find employees by role
    List<Employee> findByRole(Employee.Role role);

    // Find employees by name containing (for search functionality)
    List<Employee> findByNameContainingIgnoreCase(String name);

    // Find an employee by contact number (assuming it's unique)
    Optional<Employee> findByContactNumber(String contactNumber);

    // Check if an employee with a specific contact number exists
    boolean existsByContactNumber(String contactNumber);

    // Find an employee by their name (assuming uniqueness)
    Optional<Employee> findByName(String name);

    // Find all employees with a salary greater than a given amount
    List<Employee> findBySalaryGreaterThan(Double salary);
}
