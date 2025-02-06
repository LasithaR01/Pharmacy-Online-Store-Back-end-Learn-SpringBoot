package pharmacy.pharmacy.service;

import pharmacy.pharmacy.entity.OrderItem;
import pharmacy.pharmacy.dao.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // Create a new OrderItem
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    // Retrieve all OrderItems
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    // Retrieve an OrderItem by ID
    public Optional<OrderItem> getOrderItemById(UUID id) {
        return orderItemRepository.findById(id);
    }

    // Update an existing OrderItem
    public OrderItem updateOrderItem(UUID id, OrderItem orderItemDetails) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(id);
        if (optionalOrderItem.isPresent()) {
            OrderItem existingOrderItem = optionalOrderItem.get();
            existingOrderItem.setOrder(orderItemDetails.getOrder());
            existingOrderItem.setProduct(orderItemDetails.getProduct());
            existingOrderItem.setProductName(orderItemDetails.getProductName());
            existingOrderItem.setPrice(orderItemDetails.getPrice());
            existingOrderItem.setQuantity(orderItemDetails.getQuantity());
            return orderItemRepository.save(existingOrderItem);
        } else {
            throw new RuntimeException("OrderItem not found with id: " + id);
        }
    }

    // Delete an OrderItem by ID
    public void deleteOrderItem(UUID id) {
        orderItemRepository.deleteById(id);
    }
}