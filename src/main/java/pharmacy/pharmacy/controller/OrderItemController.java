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
import pharmacy.pharmacy.dto.OrderItemDTO;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.OrderItemService;

@RestController
@RequestMapping("/api/order-items")
@Tag(name = "Order Item Management", description = "Endpoints for managing order items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @Operation(summary = "Add item to order", description = "Add a new item to an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order or product not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<OrderItemDTO> addItemToOrder(
            @Parameter(description = "Order item to be added") @RequestBody OrderItemDTO orderItemDTO) {
        try {
            return ResponseEntity.ok(orderItemService.addItemToOrder(orderItemDTO));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error adding item to order", e);
        }
    }

    @Operation(summary = "Update item quantity", description = "Update the quantity of an existing order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantity updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid quantity",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order item not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}/quantity")
    public ResponseEntity<OrderItemDTO> updateItemQuantity(
            @Parameter(description = "ID of the order item") @PathVariable int id,
            @Parameter(description = "New quantity") @RequestParam int quantity) {
        try {
            return ResponseEntity.ok(orderItemService.updateOrderItemQuantity(id, quantity));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating quantity for order item id: " + id, e);
        }
    }

    @Operation(summary = "Remove item from order", description = "Remove an item from an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeItemFromOrder(
            @Parameter(description = "ID of the order item to remove") @PathVariable int id) {
        try {
            orderItemService.removeItemFromOrder(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error removing order item with id: " + id, e);
        }
    }
}