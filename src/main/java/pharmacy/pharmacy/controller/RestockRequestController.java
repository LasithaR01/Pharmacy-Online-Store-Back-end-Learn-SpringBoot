package pharmacy.pharmacy.controller;

import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.RestockRequestDTO;
import pharmacy.pharmacy.dto.restockRequest.RestockRequestCreateRequest;
import pharmacy.pharmacy.enums.RestockStatus;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.RestockRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/restock-requests")
@Tag(name = "Restock Request Management", description = "Endpoints for managing restock requests")
public class RestockRequestController {

    private final RestockRequestService restockRequestService;

    public RestockRequestController(RestockRequestService restockRequestService) {
        this.restockRequestService = restockRequestService;
    }

    @Operation(summary = "Get all restock requests", description = "Retrieve a list of all restock requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockRequestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<RestockRequestDTO>> getAllRestockRequests() {
        try {
            return ResponseEntity.ok(restockRequestService.getAllRestockRequests());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve restock requests", e);
        }
    }

    @Operation(summary = "Get restock request by ID", description = "Retrieve a specific restock request by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restock request found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockRequestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restock request not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestockRequestDTO> getRestockRequestById(
            @Parameter(description = "ID of the restock request to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(restockRequestService.getRestockRequestById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve restock request with id: " + id, e);
        }
    }

    @Operation(summary = "Create restock request", description = "Create a new restock request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restock request created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<RestockRequestDTO> createRestockRequest(
            @Parameter(description = "Restock request data to be created") @Valid @RequestBody RestockRequestCreateRequest requestDTO) {
        return ResponseEntity.ok(restockRequestService.createRestockRequest(requestDTO));
    }

    @Operation(summary = "Approve restock request", description = "Approve a pending restock request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restock request approved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Restock request cannot be approved",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restock request not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/{id}/approve")
    public ResponseEntity<RestockRequestDTO> approveRequest(
            @Parameter(description = "ID of the restock request to approve") @PathVariable int id
//            @Parameter(description = "ID of the user approving the request") @RequestParam int approvedById
    ) {
        return ResponseEntity.ok(restockRequestService.approveRequest(id));
    }

    @Operation(summary = "Approve multiple restock requests", description = "Approve multiple pending restock requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restock requests approved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/approve-multiple")
    public ResponseEntity<Void> approveMultipleRequests(
            @Parameter(description = "List of restock request IDs to approve") @RequestBody List<Integer> requestIds,
            @Parameter(description = "ID of the user approving the requests") @RequestParam int approvedById) {
        try {
            restockRequestService.approveMultipleRequests(requestIds, approvedById);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to approve restock requests", e);
        }
    }

    @Operation(summary = "Get restock requests by status", description = "Retrieve restock requests by their status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved restock requests",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockRequestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RestockRequestDTO>> getRequestsByStatus(
            @Parameter(description = "Status to filter by") @PathVariable RestockStatus status) {
        try {
            return ResponseEntity.ok(restockRequestService.getRequestsByStatus(status));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve restock requests by status", e);
        }
    }

    @Operation(summary = "Get active restock requests", description = "Retrieve active restock requests (pending or approved)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved active restock requests",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestockRequestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/active")
    public ResponseEntity<List<RestockRequestDTO>> getActiveRequests() {
        try {
            return ResponseEntity.ok(restockRequestService.getActiveRequests());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve active restock requests", e);
        }
    }
}