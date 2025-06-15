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
import pharmacy.pharmacy.dto.SupplierDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.SupplierService;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@Tag(name = "Supplier Management", description = "Endpoints for managing pharmacy suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(summary = "Get all suppliers", description = "Retrieve a list of all suppliers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupplierDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        try {
            return ResponseEntity.ok(supplierService.getAllSuppliers());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving suppliers", e);
        }
    }

    @Operation(summary = "Get supplier by ID", description = "Retrieve a specific supplier by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupplierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(
            @Parameter(description = "ID of the supplier to be retrieved") @PathVariable Integer id) {
        try {
            return ResponseEntity.ok(supplierService.getSupplierById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving supplier with id: " + id, e);
        }
    }

    @Operation(summary = "Create a new supplier", description = "Register a new supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupplierDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(
            @Parameter(description = "Supplier object to be created") @RequestBody SupplierDTO supplierDTO) {
        try {
            return ResponseEntity.ok(supplierService.createSupplier(supplierDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error creating supplier", e);
        }
    }

    @Operation(summary = "Update supplier", description = "Update an existing supplier's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupplierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(
            @Parameter(description = "ID of the supplier to be updated") @PathVariable Integer id,
            @Parameter(description = "Updated supplier object") @RequestBody SupplierDTO supplierDTO) {
        try {
            return ResponseEntity.ok(supplierService.updateSupplier(id, supplierDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating supplier with id: " + id, e);
        }
    }

    @Operation(summary = "Delete supplier", description = "Remove a supplier from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(
            @Parameter(description = "ID of the supplier to be deleted") @PathVariable Integer id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting supplier with id: " + id, e);
        }
    }
}