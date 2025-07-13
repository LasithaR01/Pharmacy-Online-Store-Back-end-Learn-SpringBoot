package pharmacy.pharmacy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.sentry.Sentry;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.EmployeeDTO;
import pharmacy.pharmacy.dto.employee.EmployeeCreateRequest;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "Endpoints for managing pharmacy employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Get all employees", description = "Retrieve a list of all pharmacy employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        try {
            return ResponseEntity.ok(employeeService.getAllEmployees());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving employees", e);
        }
    }

    @Operation(summary = "Get employee by ID", description = "Retrieve a specific employee by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(
            @Parameter(description = "ID of the employee to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(employeeService.getEmployeeById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving employee with id: " + id, e);
        }
    }

    @Operation(summary = "Get employees by branch",
               description = "Retrieve all employees working at a specific branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Branch not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByBranch(
            @Parameter(description = "ID of the branch") @PathVariable int branchId) {
        try {
            return ResponseEntity.ok(employeeService.getEmployeesByBranch(branchId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving employees for branch with id: " + branchId, e);
        }
    }

    @Operation(summary = "Get employees by position",
               description = "Retrieve all employees with a specific position")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/position/{position}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPosition(
            @Parameter(description = "Position name (e.g., Pharmacist, Cashier)") @PathVariable String position) {
        try {
            return ResponseEntity.ok(employeeService.getEmployeesByPosition(position));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving employees by position: " + position, e);
        }
    }

    @Operation(summary = "Create a new employee", description = "Register a new pharmacy employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User or Branch not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "User is already an employee",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Parameter(description = "Employee object to be created") @RequestBody @Valid EmployeeCreateRequest request) {
        return ResponseEntity.ok(employeeService.createEmployee(request));
    }

    @Operation(summary = "Update employee", description = "Update an existing employee's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @Parameter(description = "ID of the employee to be updated") @PathVariable int id,
            @Parameter(description = "Updated employee object") @RequestBody EmployeeDTO employeeDTO) {
        try {
            return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating employee with id: " + id, e);
        }
    }

    @Operation(summary = "Delete employee", description = "Remove an employee from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "ID of the employee to be deleted") @PathVariable int id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting employee with id: " + id, e);
        }
    }

    @Operation(summary = "Get employee by user ID",
               description = "Retrieve employee information for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found for this user",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<EmployeeDTO> getEmployeeByUserId(
            @Parameter(description = "ID of the user") @PathVariable int userId) {
        try {
            return ResponseEntity.ok(employeeService.getEmployeeByUserId(userId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving employee for user id: " + userId, e);
        }
    }
}