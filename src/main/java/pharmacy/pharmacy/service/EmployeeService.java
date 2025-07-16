package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.BranchRepository;
import pharmacy.pharmacy.dao.EmployeeRepository;
import pharmacy.pharmacy.dao.UserRepository;
import pharmacy.pharmacy.dao.UserRoleRepository;
import pharmacy.pharmacy.dto.EmployeeDTO;
import pharmacy.pharmacy.dto.employee.EmployeeCreateRequest;
import pharmacy.pharmacy.entity.*;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final BranchService branchService;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           UserRepository userRepository,
                           BranchRepository branchRepository,
                           PasswordEncoder passwordEncoder,
                           BranchService branchService,
                           UserRoleRepository userRoleRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.branchService = branchService;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
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

    @Transactional
    public EmployeeDTO createEmployee(EmployeeCreateRequest request) {

        Branch branch = request.getBranchId() != null ? branchService.getBranchEntityById(request.getBranchId()) : null;
        Set<UserRole> userRoles = new HashSet<>();

        ERole role;
        try {
            role = ERole.valueOf(request.getPosition().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid role: " + request.getPosition());
        }

        UserRole userRole = userRoleRepository.findByName(role)
                .orElseThrow(() -> new ResourceNotFoundException("User Role not found for: " + role));

        userRoles.add(userRole);

        // create User
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .name(request.getName())
                .build();
        user.setRoles(userRoles);
        user = userRepository.save(user);

        // create Employee
        Employee employee = Employee.builder()
                .user(user)
                .branch(branch)
                .position(request.getPosition())
                .salary(request.getSalary())
                .hireDate(request.getHireDate())
                .build();
        employee = employeeRepository.save(employee);

        return EntityDtoMapper.convertToEmployeeDTO(employee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(int id, EmployeeDTO employeeDTO) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        User user = employee.getUser();

        employee.setBranch(branchService.getBranchEntityById(employeeDTO.getBranchId()));
        employee.setPosition(employeeDTO.getPosition());
        employee.setSalary(employeeDTO.getSalary());

        user.setUsername(employeeDTO.getUsername());
        user.setEmail(employeeDTO.getEmail());
        user.setName(employeeDTO.getName());
        user.setPhoneNumber(employeeDTO.getPhoneNumber());

        userRepository.save(user);
        employeeRepository.save(employee);
        return EntityDtoMapper.convertToEmployeeDTO(employee);
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