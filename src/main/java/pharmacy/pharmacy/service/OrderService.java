package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.OrderRepository;
//import pharmacy.pharmacy.dto.OrderRequest;
import pharmacy.pharmacy.entity.Order;
//import pharmacy.pharmacy.exception.OrderSaveException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Fetch all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Fetch an order by ID
    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    // Fetch an order by slug
    public Optional<Order> getOrderBySlug(String slug) {
        return orderRepository.findBySlug(slug);
    }

    // Save or update an order
//    public Order saveOrder(OrderRequest orderRequest) {
//        try {
//            // Generate the slug from the name
//            String slug = orderRequest.getName().toLowerCase()
//                    .replaceAll("[^a-z0-9]+", "-")
//                    .replaceAll("-$", "");
//
//            // Create or update an Order entity
//            Order order = new Order();
//            order.setName(orderRequest.getName());
//            order.setUserId(orderRequest.getUserId());
//            order.setOrderDate(orderRequest.getOrderDate());
//            order.setTotalAmount(orderRequest.getTotalAmount());
//            order.setStatus(orderRequest.getStatus());
//            order.setSlug(slug);
//            order.setCreatedAt(new Date());
//
//            return orderRepository.save(order);
//
//        } catch (DataIntegrityViolationException ex) {
//            // Handle database constraint violations
//            throw new OrderSaveException("Database error while saving the order: " + ex.getMessage(), ex);
//        } catch (NullPointerException ex) {
//            // Handle null pointer exceptions
//            throw new OrderSaveException("Order or its name cannot be null", ex);
//        } catch (Exception ex) {
//            // Catch other unexpected exceptions
//            throw new OrderSaveException("An unexpected error occurred while saving the order", ex);
//        }
//    }

    // Delete an order by ID
    public void deleteOrderById(UUID id) {
        orderRepository.deleteById(id);
    }
}
