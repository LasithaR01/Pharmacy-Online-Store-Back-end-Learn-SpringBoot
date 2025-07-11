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
import pharmacy.pharmacy.dto.OrderDTO;
import pharmacy.pharmacy.dto.OrderItemDTO;
import pharmacy.pharmacy.enums.OrderStatus;
import pharmacy.pharmacy.enums.PaymentMethod;
import pharmacy.pharmacy.enums.PaymentStatus;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Endpoints for managing pharmacy orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(
            @Parameter(description = "ID of the order to be retrieved") @PathVariable int id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving order with id: " + id, e);
        }
    }

    @Operation(summary = "Create a new order", description = "Create a new pharmacy order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @Parameter(description = "Order object to be created") @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    @Operation(summary = "Update order status", description = "Update the status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @Parameter(description = "ID of the order to be updated") @PathVariable int id,
            @Parameter(description = "New order status") @RequestParam OrderStatus status) {
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating order status for id: " + id, e);
        }
    }

    @Operation(summary = "Update payment status", description = "Update the payment status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<OrderDTO> updatePaymentStatus(
            @Parameter(description = "ID of the order to be updated") @PathVariable int id,
            @Parameter(description = "New payment status") @RequestParam PaymentStatus paymentStatus) {
        try {
            return ResponseEntity.ok(orderService.updatePaymentStatus(id, paymentStatus));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating payment status for order id: " + id, e);
        }
    }

    @Operation(summary = "Checkout order", description = "Checkout an existing cart order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order checked out successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid operation",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/{id}/checkout")
    public ResponseEntity<OrderDTO> checkoutOrder(
            @Parameter(description = "ID of the order to checkout") @PathVariable int id,
            @Parameter(description = "Payment method") @RequestParam PaymentMethod paymentMethod) {
        try {
            return ResponseEntity.ok(orderService.checkoutOrder(id, paymentMethod));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error checking out order with id: " + id, e);
        }
    }

    @Operation(summary = "Delete order", description = "Delete an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid operation",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID of the order to be deleted") @PathVariable int id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting order with id: " + id, e);
        }
    }

    @Operation(summary = "Get order items", description = "Get all items for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(
            @Parameter(description = "ID of the order") @PathVariable int id) {
        try {
            return ResponseEntity.ok(orderService.getItemsByOrderId(id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving items for order id: " + id, e);
        }
    }
}