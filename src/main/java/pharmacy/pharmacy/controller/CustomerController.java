package pharmacy.pharmacy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.sentry.Sentry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.CustomerDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "Endpoints for managing pharmacy customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get all customers", description = "Retrieve a list of all pharmacy customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        try {
            return ResponseEntity.ok(customerService.getAllCustomers());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving customers", e);
        }
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(
            @Parameter(description = "ID of the customer to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(customerService.getCustomerById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving customer with id: " + id, e);
        }
    }

    @Operation(summary = "Get customer by user ID",
               description = "Retrieve customer information for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found for this user",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomerDTO> getCustomerByUserId(
            @Parameter(description = "ID of the user") @PathVariable int userId) {
        try {
            return ResponseEntity.ok(customerService.getCustomerByUserId(userId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving customer for user id: " + userId, e);
        }
    }

    @Operation(summary = "Create a new customer", description = "Register a new pharmacy customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "User is already a customer",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(
            @Parameter(description = "Customer object to be created") @RequestBody CustomerDTO customerDTO) {
        try {
            return ResponseEntity.ok(customerService.createCustomer(customerDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error creating customer", e);
        }
    }

    @Operation(summary = "Update customer", description = "Update an existing customer's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer to be updated") @PathVariable int id,
            @Parameter(description = "Updated customer object") @RequestBody CustomerDTO customerDTO) {
        try {
            return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating customer with id: " + id, e);
        }
    }

    @Operation(summary = "Delete customer", description = "Remove a customer from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to be deleted") @PathVariable int id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting customer with id: " + id, e);
        }
    }

    @Operation(summary = "Get customers by loyalty points range",
               description = "Retrieve customers with loyalty points within a specific range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid range parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/loyalty")
    public ResponseEntity<List<CustomerDTO>> getCustomersByLoyaltyPointsRange(
            @Parameter(description = "Minimum loyalty points") @RequestParam int minPoints,
            @Parameter(description = "Maximum loyalty points") @RequestParam int maxPoints) {
        try {
            return ResponseEntity.ok(customerService.getCustomersByLoyaltyPointsRange(minPoints, maxPoints));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving customers by loyalty points range", e);
        }
    }

    @Operation(summary = "Add loyalty points",
               description = "Add loyalty points to a customer's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Points added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid points value",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/{id}/loyalty/add")
    public ResponseEntity<CustomerDTO> addLoyaltyPoints(
            @Parameter(description = "ID of the customer") @PathVariable int id,
            @Parameter(description = "Points to add") @RequestParam int points) {
        try {
            return ResponseEntity.ok(customerService.addLoyaltyPoints(id, points));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error adding loyalty points to customer with id: " + id, e);
        }
    }

    @Operation(summary = "Redeem loyalty points",
               description = "Redeem loyalty points from a customer's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Points redeemed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid points value or insufficient points",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/{id}/loyalty/redeem")
    public ResponseEntity<CustomerDTO> redeemLoyaltyPoints(
            @Parameter(description = "ID of the customer") @PathVariable int id,
            @Parameter(description = "Points to redeem") @RequestParam int points) {
        try {
            return ResponseEntity.ok(customerService.redeemLoyaltyPoints(id, points));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error redeeming loyalty points for customer with id: " + id, e);
        }
    }
}