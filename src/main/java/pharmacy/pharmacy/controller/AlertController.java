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
import pharmacy.pharmacy.dto.AlertDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.AlertService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alert Management", description = "Endpoints for managing alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @Operation(summary = "Get all alerts", description = "Retrieve a list of all alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AlertDTO>> getAllAlerts() {
        try {
            return ResponseEntity.ok(alertService.getAllAlerts());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve alerts", e);
        }
    }

    @Operation(summary = "Get alert by ID", description = "Retrieve a specific alert by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))),
            @ApiResponse(responseCode = "404", description = "Alert not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlertDTO> getAlertById(
            @Parameter(description = "ID of the alert to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(alertService.getAlertById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve alert with id: " + id, e);
        }
    }

    @Operation(summary = "Create alert", description = "Create a new alert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<AlertDTO> createAlert(
            @Parameter(description = "Alert data to be created") @RequestBody AlertDTO alertDTO) {
        try {
            return ResponseEntity.ok(alertService.createAlert(alertDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create alert", e);
        }
    }

    @Operation(summary = "Resolve alert", description = "Mark an alert as resolved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert resolved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))),
            @ApiResponse(responseCode = "400", description = "Alert cannot be resolved",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Alert not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/{id}/resolve")
    public ResponseEntity<AlertDTO> resolveAlert(
            @Parameter(description = "ID of the alert to resolve") @PathVariable int id,
            @Parameter(description = "ID of the user resolving the alert") @RequestParam int resolvedById) {
        try {
            return ResponseEntity.ok(alertService.resolveAlert(id, resolvedById));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to resolve alert with id: " + id, e);
        }
    }

    @Operation(summary = "Resolve multiple alerts", description = "Mark multiple alerts as resolved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerts resolved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/resolve-multiple")
    public ResponseEntity<Void> resolveMultipleAlerts(
            @Parameter(description = "List of alert IDs to resolve") @RequestBody List<Integer> alertIds,
            @Parameter(description = "ID of the user resolving the alerts") @RequestParam int resolvedById) {
        try {
            alertService.resolveMultipleAlerts(alertIds, resolvedById);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to resolve alerts", e);
        }
    }

    @Operation(summary = "Get unresolved alerts", description = "Retrieve all unresolved alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved unresolved alerts",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/unresolved")
    public ResponseEntity<List<AlertDTO>> getUnresolvedAlerts() {
        try {
            return ResponseEntity.ok(alertService.getUnresolvedAlerts());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve unresolved alerts", e);
        }
    }

    @Operation(summary = "Get critical alerts", description = "Retrieve all critical alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved critical alerts",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/critical")
    public ResponseEntity<List<AlertDTO>> getCriticalAlerts() {
        try {
            return ResponseEntity.ok(alertService.getCriticalAlerts());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve critical alerts", e);
        }
    }

    @Operation(summary = "Cleanup resolved alerts", description = "Delete resolved alerts older than specified date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alerts cleaned up successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> cleanupResolvedAlerts(
            @Parameter(description = "Cutoff date (older alerts will be deleted)") @RequestParam Date cutoffDate) {
        try {
            alertService.cleanupResolvedAlerts(cutoffDate);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to cleanup resolved alerts", e);
        }
    }
}