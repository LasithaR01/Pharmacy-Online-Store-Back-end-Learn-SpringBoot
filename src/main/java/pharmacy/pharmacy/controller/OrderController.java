package pharmacy.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.OrderRequest;
import pharmacy.pharmacy.entity.Order;
import pharmacy.pharmacy.service.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get an order by slug
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Order> getOrderBySlug(@PathVariable String slug) {
        Optional<Order> order = orderService.getOrderBySlug(slug);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new order
    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.saveOrder(orderRequest);
    }

    // Update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable UUID id, @RequestBody OrderRequest orderRequest) {
        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    orderRequest.setName(orderRequest.getName());
                    orderRequest.setOrderDate(orderRequest.getOrderDate());
                    orderRequest.setTotalAmount(orderRequest.getTotalAmount());
                    orderRequest.setStatus(orderRequest.getStatus());
                    return ResponseEntity.ok(orderService.saveOrder(orderRequest));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete an order by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrderById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
