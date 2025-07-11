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
import pharmacy.pharmacy.dto.PrescriptionDTO;
import pharmacy.pharmacy.enums.PrescriptionStatus;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.PrescriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@Tag(name = "Prescription Management", description = "Endpoints for managing prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @Operation(summary = "Get all prescriptions", description = "Retrieve a list of all prescriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions() {
        try {
            return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve prescriptions", e);
        }
    }

    @Operation(summary = "Get prescription by ID", description = "Retrieve a specific prescription by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Prescription not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(
            @Parameter(description = "ID of the prescription to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve prescription with id: " + id, e);
        }
    }

    @Operation(summary = "Create new prescription", description = "Create a new prescription record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<PrescriptionDTO> createPrescription(
            @Parameter(description = "Prescription data to be created") @RequestBody PrescriptionDTO prescriptionDTO) {
        try {
            return ResponseEntity.ok(prescriptionService.createPrescription(prescriptionDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create prescription", e);
        }
    }

    @Operation(summary = "Approve prescription", description = "Approve a pending prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription approved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Prescription cannot be approved",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Prescription not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/{id}/approve")
    public ResponseEntity<PrescriptionDTO> approvePrescription(
            @Parameter(description = "ID of the prescription to approve") @PathVariable int id,
            @Parameter(description = "ID of the approving user") @RequestParam int approvedById) {
        try {
            return ResponseEntity.ok(prescriptionService.approvePrescription(id, approvedById));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to approve prescription with id: " + id, e);
        }
    }

    @Operation(summary = "Reject prescription", description = "Reject a pending prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription rejected successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Prescription cannot be rejected",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Prescription not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/{id}/reject")
    public ResponseEntity<PrescriptionDTO> rejectPrescription(
            @Parameter(description = "ID of the prescription to reject") @PathVariable int id,
            @Parameter(description = "ID of the rejecting user") @RequestParam int rejectedById) {
        try {
            return ResponseEntity.ok(prescriptionService.rejectPrescription(id, rejectedById));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to reject prescription with id: " + id, e);
        }
    }

    @Operation(summary = "Delete prescription", description = "Delete a prescription record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescription deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Prescription cannot be deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Prescription not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(
            @Parameter(description = "ID of the prescription to delete") @PathVariable int id) {
        try {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete prescription with id: " + id, e);
        }
    }

    @Operation(summary = "Get prescriptions by user", description = "Retrieve all prescriptions for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved prescriptions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByUser(
            @Parameter(description = "ID of the user") @PathVariable int userId) {
        try {
            return ResponseEntity.ok(prescriptionService.getPrescriptionsByUser(userId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve prescriptions for user id: " + userId, e);
        }
    }

    @Operation(summary = "Get prescriptions by status", description = "Retrieve all prescriptions with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved prescriptions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByStatus(
            @Parameter(description = "Status to filter by") @PathVariable PrescriptionStatus status) {
        try {
            return ResponseEntity.ok(prescriptionService.getPrescriptionsByStatus(status));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve prescriptions by status: " + status, e);
        }
    }
}