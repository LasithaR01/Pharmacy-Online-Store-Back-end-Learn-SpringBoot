package pharmacy.pharmacy.controller;

import pharmacy.pharmacy.dto.OrderItemDTO;
import pharmacy.pharmacy.dto.OrderItemRequest;
import pharmacy.pharmacy.entity.OrderItem;
import pharmacy.pharmacy.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orderItems")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Create OrderItem
    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemRequest orderItemRequest) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductName(orderItemRequest.getProductId().toString()); // Adjust based on your entity structure
        orderItem.setQuantity(orderItemRequest.getQuantity());

        OrderItem savedOrderItem = orderItemService.createOrderItem(orderItem);
        return ResponseEntity.ok(new OrderItemDTO(savedOrderItem));
    }

    // Get all OrderItems
    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        List<OrderItemDTO> orderItems = orderItemService.getAllOrderItems()
                .stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderItems);
    }

    // Get OrderItem by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable UUID id) {
        return orderItemService.getOrderItemById(id)
                .map(orderItem -> ResponseEntity.ok(new OrderItemDTO(orderItem)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Update OrderItem
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable UUID id,
            @RequestBody OrderItemRequest orderItemRequest) {

        OrderItem orderItemDetails = new OrderItem();
        orderItemDetails.setProductName(orderItemRequest.getProductId().toString());
        orderItemDetails.setQuantity(orderItemRequest.getQuantity());

        OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItemDetails);
        return ResponseEntity.ok(new OrderItemDTO(updatedOrderItem));
    }

    // Delete OrderItem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable UUID id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
