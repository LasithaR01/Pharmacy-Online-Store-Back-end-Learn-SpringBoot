package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
//import pharmacy.pharmacy.dao.BranchRepository;
import pharmacy.pharmacy.dao.EmployeeRepository;
import pharmacy.pharmacy.dto.EmployeeRequest;
//import pharmacy.pharmacy.entity.Branch;
import pharmacy.pharmacy.entity.Employee;
import pharmacy.pharmacy.exception.EmployeeSaveException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

//    @Autowired
//    private BranchRepository branchRepository;

    // Fetch all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Fetch an employee by ID
    public Optional<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id);
    }

    // Save or update an employee
    public Employee saveEmployee(EmployeeRequest employeeRequest) {
//        Branch branch = branchRepository.findById(employeeRequest.getBranchId())
//                .orElseThrow(() -> new RuntimeException("Branch not found"));

        try {
            Employee employee = new Employee();
            employee.setName(employeeRequest.getName());
            employee.setRole(Employee.Role.valueOf(employeeRequest.getRole())); // Convert role from string to Enum
            employee.setContactNumber(employeeRequest.getContactNumber());
            employee.setSalary(employeeRequest.getSalary());
         //   employee.setBranch(branch);

            return employeeRepository.save(employee);

        } catch (IllegalArgumentException ex) {
            // Handle invalid enum value
            throw new EmployeeSaveException("Invalid role value provided", ex);
        } catch (DataIntegrityViolationException ex) {
            // Handle database constraint violations
            throw new EmployeeSaveException("Database error while saving employee: " + ex.getMessage(), ex);
        } catch (NullPointerException ex) {
            // Handle null pointer exception
            throw new EmployeeSaveException("Employee details cannot be null", ex);
        } catch (Exception ex) {
            // Catch other unexpected exceptions
            throw new EmployeeSaveException("An unexpected error occurred while saving the employee", ex);
        }
    }

    // Delete an employee by ID
    public void deleteEmployeeById(UUID id) {
        employeeRepository.deleteById(id);
    }
}
