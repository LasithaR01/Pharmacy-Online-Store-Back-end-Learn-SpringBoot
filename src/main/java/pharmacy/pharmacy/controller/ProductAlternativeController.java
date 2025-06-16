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
import pharmacy.pharmacy.dto.ProductAlternativeDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.ProductAlternativeService;

import java.util.List;

@RestController
@RequestMapping("/api/product-alternatives")
@Tag(name = "Product Alternatives Management", description = "Endpoints for managing product alternative relationships")
public class ProductAlternativeController {

    private final ProductAlternativeService productAlternativeService;

    public ProductAlternativeController(ProductAlternativeService productAlternativeService) {
        this.productAlternativeService = productAlternativeService;
    }

    @Operation(summary = "Get all alternatives", description = "Retrieve all product alternative relationships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductAlternativeDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ProductAlternativeDTO>> getAllAlternatives() {
        try {
            return ResponseEntity.ok(productAlternativeService.getAllAlternatives());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving product alternatives", e);
        }
    }

    @Operation(summary = "Get alternative by ID", description = "Retrieve a specific alternative relationship by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alternative found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductAlternativeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Alternative not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductAlternativeDTO> getAlternativeById(
            @Parameter(description = "ID of the alternative relationship") @PathVariable int id) {
        try {
            return ResponseEntity.ok(productAlternativeService.getAlternativeById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving alternative with id: " + id, e);
        }
    }

    @Operation(summary = "Get alternatives for product", description = "Retrieve all alternatives for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductAlternativeDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductAlternativeDTO>> getAlternativesForProduct(
            @Parameter(description = "ID of the product") @PathVariable int productId) {
        try {
            return ResponseEntity.ok(productAlternativeService.getAlternativesForProduct(productId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving alternatives for product id: " + productId, e);
        }
    }

    @Operation(summary = "Create alternative", description = "Create a new product alternative relationship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alternative created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductAlternativeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductAlternativeDTO> createAlternative(
            @Parameter(description = "Alternative object to be created") @RequestBody ProductAlternativeDTO alternativeDTO) {
        try {
            return ResponseEntity.ok(productAlternativeService.createAlternative(alternativeDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error creating product alternative", e);
        }
    }

    @Operation(summary = "Update alternative", description = "Update an existing product alternative relationship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alternative updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductAlternativeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Alternative not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductAlternativeDTO> updateAlternative(
            @Parameter(description = "ID of the alternative to be updated") @PathVariable int id,
            @Parameter(description = "Updated alternative object") @RequestBody ProductAlternativeDTO alternativeDTO) {
        try {
            return ResponseEntity.ok(productAlternativeService.updateAlternative(id, alternativeDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating alternative with id: " + id, e);
        }
    }

    @Operation(summary = "Delete alternative", description = "Delete a product alternative relationship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alternative deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Alternative not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlternative(
            @Parameter(description = "ID of the alternative to be deleted") @PathVariable int id) {
        try {
            productAlternativeService.deleteAlternative(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting alternative with id: " + id, e);
        }
    }

    @Operation(summary = "Get recommended alternatives",
              description = "Get recommended alternatives for a product (in stock, same category, cheaper)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recommendations",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductAlternativeDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/recommendations/{productId}")
    public ResponseEntity<List<ProductAlternativeDTO>> getRecommendedAlternatives(
            @Parameter(description = "ID of the product") @PathVariable int productId) {
        try {
            return ResponseEntity.ok(productAlternativeService.getRecommendedAlternatives(productId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error getting recommendations for product id: " + productId, e);
        }
    }
}