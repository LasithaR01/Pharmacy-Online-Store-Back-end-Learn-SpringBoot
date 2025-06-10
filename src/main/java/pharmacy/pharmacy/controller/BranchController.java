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
import pharmacy.pharmacy.dto.BranchDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.BranchService;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@Tag(name = "Branch Management", description = "Endpoints for managing pharmacy branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @Operation(summary = "Get all branches", description = "Retrieve a list of all pharmacy branches")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BranchDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        try {
            return ResponseEntity.ok(branchService.getAllBranches());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving branches", e);
        }
    }

    @Operation(summary = "Get branch by ID", description = "Retrieve a specific branch by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BranchDTO.class))),
            @ApiResponse(responseCode = "404", description = "Branch not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getBranchById(
            @Parameter(description = "ID of the branch to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(branchService.getBranchById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving branch with id: " + id, e);
        }
    }

    @Operation(summary = "Create a new branch", description = "Register a new pharmacy branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BranchDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(
            @Parameter(description = "Branch object to be created") @RequestBody BranchDTO branchDTO) {
        try {
            return ResponseEntity.ok(branchService.createBranch(branchDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error creating branch", e);
        }
    }

    @Operation(summary = "Update branch", description = "Update an existing branch's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BranchDTO.class))),
            @ApiResponse(responseCode = "404", description = "Branch not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> updateBranch(
            @Parameter(description = "ID of the branch to be updated") @PathVariable int id,
            @Parameter(description = "Updated branch object") @RequestBody BranchDTO branchDTO) {
        try {
            return ResponseEntity.ok(branchService.updateBranch(id, branchDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating branch with id: " + id, e);
        }
    }

    @Operation(summary = "Delete branch", description = "Remove a branch from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Branch not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(
            @Parameter(description = "ID of the branch to be deleted") @PathVariable int id) {
        try {
            branchService.deleteBranch(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting branch with id: " + id, e);
        }
    }
}