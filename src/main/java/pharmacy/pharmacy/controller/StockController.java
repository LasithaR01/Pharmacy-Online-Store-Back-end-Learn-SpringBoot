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
import pharmacy.pharmacy.dto.StockDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@Tag(name = "Stock Management", description = "Endpoints for managing pharmacy stock entries")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Get all stock entries", description = "Retrieve a list of all stock entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStockEntries() {
        try {
            return ResponseEntity.ok(stockService.getAllStockEntries());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving stock entries", e);
        }
    }

    @Operation(summary = "Get stock entry by ID", description = "Retrieve a specific stock entry by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock entry found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class))),
            @ApiResponse(responseCode = "404", description = "Stock entry not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockById(
            @Parameter(description = "ID of the stock entry to be retrieved") @PathVariable Integer id) {
        try {
            return ResponseEntity.ok(stockService.getStockById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving stock entry with id: " + id, e);
        }
    }

    @Operation(summary = "Get stock entries by product", description = "Retrieve stock entries for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockDTO>> getStockByProduct(
            @Parameter(description = "ID of the product") @PathVariable Integer productId) {
        try {
            return ResponseEntity.ok(stockService.getStockByProduct(productId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving stock entries for product id: " + productId, e);
        }
    }

    @Operation(summary = "Get stock entries by branch", description = "Retrieve stock entries for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<StockDTO>> getStockByBranch(
            @Parameter(description = "ID of the branch") @PathVariable Integer branchId) {
        try {
            return ResponseEntity.ok(stockService.getStockByBranch(branchId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving stock entries for branch id: " + branchId, e);
        }
    }

    @Operation(summary = "Create a new stock entry", description = "Register a new stock entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock entry created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<StockDTO> createStock(
            @Parameter(description = "Stock object to be created") @RequestBody StockDTO stockDTO) {
        try {
            return ResponseEntity.ok(stockService.createStock(stockDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error creating stock entry", e);
        }
    }

    @Operation(summary = "Update stock entry", description = "Update an existing stock entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock entry updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockDTO.class))),
            @ApiResponse(responseCode = "404", description = "Stock entry not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(
            @Parameter(description = "ID of the stock entry to be updated") @PathVariable Integer id,
            @Parameter(description = "Updated stock object") @RequestBody StockDTO stockDTO) {
        try {
            return ResponseEntity.ok(stockService.updateStock(id, stockDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating stock entry with id: " + id, e);
        }
    }

    @Operation(summary = "Delete stock entry", description = "Remove a stock entry from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock entry deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Stock entry not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(
            @Parameter(description = "ID of the stock entry to be deleted") @PathVariable Integer id) {
        try {
            stockService.deleteStock(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting stock entry with id: " + id, e);
        }
    }
}