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
import pharmacy.pharmacy.dto.DrugInteractionDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.DrugInteractionService;

import java.util.List;

@RestController
@RequestMapping("/api/interactions")
@Tag(name = "Drug Interaction Management", description = "Endpoints for managing drug interactions")
public class DrugInteractionController {

    private final DrugInteractionService interactionService;

    public DrugInteractionController(DrugInteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @Operation(summary = "Get all interactions", description = "Retrieve a list of all drug interactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrugInteractionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<DrugInteractionDTO>> getAllInteractions() {
        try {
            return ResponseEntity.ok(interactionService.getAllInteractions());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving drug interactions", e);
        }
    }

    @Operation(summary = "Get interaction by ID", description = "Retrieve a specific interaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interaction found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrugInteractionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Interaction not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DrugInteractionDTO> getInteractionById(
            @Parameter(description = "ID of the interaction to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(interactionService.getInteractionById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving interaction with id: " + id, e);
        }
    }

    @Operation(summary = "Get interactions for a product",
               description = "Retrieve all interactions involving a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interactions found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrugInteractionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<DrugInteractionDTO>> getInteractionsForProduct(
            @Parameter(description = "ID of the product") @PathVariable int productId) {
        try {
            return ResponseEntity.ok(interactionService.getInteractionsForProduct(productId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving interactions for product with id: " + productId, e);
        }
    }

    @Operation(summary = "Get interactions between two products",
               description = "Retrieve interactions between two specific products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interactions found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrugInteractionDTO.class))),
            @ApiResponse(responseCode = "404", description = "One or both products not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/between/{productId1}/{productId2}")
    public ResponseEntity<List<DrugInteractionDTO>> getInteractionsBetweenProducts(
            @Parameter(description = "ID of the first product") @PathVariable int productId1,
            @Parameter(description = "ID of the second product") @PathVariable int productId2) {
        try {
            return ResponseEntity.ok(interactionService.getInteractionsBetweenProducts(productId1, productId2));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException(
                "Error retrieving interactions between products with ids: " + productId1 + " and " + productId2, e);
        }
    }

    @Operation(summary = "Get interactions by severity",
               description = "Retrieve interactions filtered by severity level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interactions found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrugInteractionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid severity level",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<DrugInteractionDTO>> getInteractionsBySeverity(
            @Parameter(description = "Severity level (MILD, MODERATE, SEVERE, CONTRAINDICATED)")
            @PathVariable String severity) {
        try {
            return ResponseEntity.ok(interactionService.getInteractionsBySeverity(severity));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving interactions by severity: " + severity, e);
        }
    }

    @Operation(summary = "Create a new interaction", description = "Register a new drug interaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interaction created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrugInteractionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Interaction already exists",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<DrugInteractionDTO> createInteraction(
            @Parameter(description = "Interaction object to be created") @RequestBody DrugInteractionDTO interactionDTO) {
        try {
            return ResponseEntity.ok(interactionService.createInteraction(interactionDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error creating drug interaction", e);
        }
    }

    @Operation(summary = "Update interaction", description = "Update an existing interaction's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interaction updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrugInteractionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Interaction not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<DrugInteractionDTO> updateInteraction(
            @Parameter(description = "ID of the interaction to be updated") @PathVariable int id,
            @Parameter(description = "Updated interaction object") @RequestBody DrugInteractionDTO interactionDTO) {
        try {
            return ResponseEntity.ok(interactionService.updateInteraction(id, interactionDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating interaction with id: " + id, e);
        }
    }

    @Operation(summary = "Delete interaction", description = "Remove an interaction from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Interaction not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInteraction(
            @Parameter(description = "ID of the interaction to be deleted") @PathVariable int id) {
        try {
            interactionService.deleteInteraction(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting interaction with id: " + id, e);
        }
    }
}