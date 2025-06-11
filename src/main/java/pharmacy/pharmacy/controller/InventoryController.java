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
import pharmacy.pharmacy.dto.InventoryDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory Management", description = "Endpoints for managing pharmacy inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Get all inventory records", description = "Retrieve a list of all inventory records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        try {
            return ResponseEntity.ok(inventoryService.getAllInventory());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving inventory records", e);
        }
    }

    @Operation(summary = "Get inventory by ID", description = "Retrieve a specific inventory record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory record found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inventory record not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(
            @Parameter(description = "ID of the inventory record to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(inventoryService.getInventoryById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving inventory record with id: " + id, e);
        }
    }

    @Operation(summary = "Get inventory by branch", description = "Retrieve inventory records for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByBranch(
            @Parameter(description = "ID of the branch") @PathVariable int branchId) {
        try {
            return ResponseEntity.ok(inventoryService.getInventoryByBranch(branchId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving inventory for branch id: " + branchId, e);
        }
    }

    @Operation(summary = "Get inventory by product", description = "Retrieve inventory records for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByProduct(
            @Parameter(description = "ID of the product") @PathVariable int productId) {
        try {
            return ResponseEntity.ok(inventoryService.getInventoryByProduct(productId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving inventory for product id: " + productId, e);
        }
    }

    @Operation(summary = "Create a new inventory record", description = "Register a new inventory record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(
            @Parameter(description = "Inventory object to be created") @RequestBody InventoryDTO inventoryDTO) {
        try {
            return ResponseEntity.ok(inventoryService.createInventory(inventoryDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error creating inventory record", e);
        }
    }

    @Operation(summary = "Update inventory record", description = "Update an existing inventory record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inventory record not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(
            @Parameter(description = "ID of the inventory record to be updated") @PathVariable int id,
            @Parameter(description = "Updated inventory object") @RequestBody InventoryDTO inventoryDTO) {
        try {
            return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating inventory record with id: " + id, e);
        }
    }

    @Operation(summary = "Delete inventory record", description = "Remove an inventory record from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory record deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory record not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(
            @Parameter(description = "ID of the inventory record to be deleted") @PathVariable int id) {
        try {
            inventoryService.deleteInventory(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting inventory record with id: " + id, e);
        }
    }

    @Operation(summary = "Update stock level", description = "Adjust the stock level of an inventory item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock level updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inventory record not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<InventoryDTO> updateStockLevel(
            @Parameter(description = "ID of the inventory record") @PathVariable int id,
            @Parameter(description = "Quantity to add (positive) or subtract (negative)") @RequestParam int quantity) {
        try {
            return ResponseEntity.ok(inventoryService.updateStockLevel(id, quantity));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating stock level for inventory id: " + id, e);
        }
    }

    @Operation(summary = "Get low stock items", description = "Retrieve all inventory items with low stock alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryDTO>> getLowStockItems() {
        try {
            return ResponseEntity.ok(inventoryService.getLowStockItems());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving low stock items", e);
        }
    }
}