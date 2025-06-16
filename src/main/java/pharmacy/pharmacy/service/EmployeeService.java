package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.BranchRepository;
import pharmacy.pharmacy.dao.EmployeeRepository;
import pharmacy.pharmacy.dao.UserRepository;
import pharmacy.pharmacy.dto.EmployeeDTO;
import pharmacy.pharmacy.entity.*;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                         UserRepository userRepository,
                         BranchRepository branchRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        try {
            return employeeRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToEmployeeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all employees", e);
        }
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(int id) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
            return EntityDtoMapper.convertToEmployeeDTO(employee);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve employee with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByBranch(int branchId) {
        try {
            if (!branchRepository.existsById(branchId)) {
                throw new ResourceNotFoundException("Branch not found with id: " + branchId);
            }
            return employeeRepository.findByBranchId(branchId).stream()
                    .map(EntityDtoMapper::convertToEmployeeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve employees for branch with id: " + branchId, e);
        }
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByPosition(String position) {
        try {
            return employeeRepository.findByPosition(position).stream()
                    .map(EntityDtoMapper::convertToEmployeeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve employees by position: " + position, e);
        }
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        try {
            // Validate required fields
            if (employeeDTO.getUserId() == null) {
                throw new GlobalException("User ID must be specified");
            }
            if (employeeDTO.getBranchId() == null) {
                throw new GlobalException("Branch ID must be specified");
            }
            if (employeeDTO.getPosition() == null || employeeDTO.getPosition().trim().isEmpty()) {
                throw new GlobalException("Position cannot be empty");
            }
            if (employeeDTO.getSalary() == null || employeeDTO.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
                throw new GlobalException("Salary must be a positive value");
            }
            if (employeeDTO.getHireDate() == null) {
                throw new GlobalException("Hire date must be specified");
            }

            // Check if user exists and is not already an employee
            User user = userRepository.findById(employeeDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + employeeDTO.getUserId()));

            if (employeeRepository.existsByUserId(user.getId())) {
                throw new GlobalException("User is already registered as an employee");
            }

            // Check if branch exists
            Branch branch = branchRepository.findById(employeeDTO.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + employeeDTO.getBranchId()));

            // Create new employee
            Employee employee = EntityDtoMapper.convertToEmployee(employeeDTO, user, branch);
            Employee savedEmployee = employeeRepository.save(employee);
            return EntityDtoMapper.convertToEmployeeDTO(savedEmployee);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create employee", e);
        }
    }

    public EmployeeDTO updateEmployee(int id, EmployeeDTO employeeDTO) {
        try {
            Employee existingEmployee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

            // Update fields if provided
            if (employeeDTO.getPosition() != null && !employeeDTO.getPosition().trim().isEmpty()) {
                existingEmployee.setPosition(employeeDTO.getPosition().trim());
            }
            if (employeeDTO.getSalary() != null && employeeDTO.getSalary().compareTo(BigDecimal.ZERO) > 0) {
                existingEmployee.setSalary(employeeDTO.getSalary());
            }
            if (employeeDTO.getHireDate() != null) {
                existingEmployee.setHireDate(employeeDTO.getHireDate());
            }

            // Branch and User cannot be changed in an existing employee record
            if (employeeDTO.getBranchId() != null || employeeDTO.getUserId() != null) {
                throw new GlobalException("Cannot change branch or user association for an existing employee");
            }

            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return EntityDtoMapper.convertToEmployeeDTO(updatedEmployee);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update employee with id: " + id, e);
        }
    }

    public void deleteEmployee(int id) {
        try {
            if (!employeeRepository.existsById(id)) {
                throw new ResourceNotFoundException("Employee not found with id: " + id);
            }
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete employee with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByUserId(int userId) {
        try {
            Employee employee = employeeRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user id: " + userId));
            return EntityDtoMapper.convertToEmployeeDTO(employee);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve employee for user id: " + userId, e);
        }
    }
}