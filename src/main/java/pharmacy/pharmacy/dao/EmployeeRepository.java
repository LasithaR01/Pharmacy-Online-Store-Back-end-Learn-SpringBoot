package pharmacy.pharmacy.dao;

import pharmacy.pharmacy.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // Find employees by branch
    List<Employee> findByBranchId(Integer branchId);

    // Find employees by position
    List<Employee> findByPosition(String position);

    // Find employees by salary range
    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :minSalary AND :maxSalary")
    List<Employee> findBySalaryRange(@Param("minSalary") BigDecimal minSalary,
                                    @Param("maxSalary") BigDecimal maxSalary);

    // Find employees hired after a specific date
    List<Employee> findByHireDateAfter(Date hireDate);

    // Check if user is already assigned as an employee
    boolean existsByUserId(Integer userId);

    // Find employee by user ID
    Optional<Employee> findByUserId(Integer userId);
}