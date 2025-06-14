package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.OrderItemRepository;
import pharmacy.pharmacy.dto.OrderItemDTO;
import pharmacy.pharmacy.entity.*;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    private final ProductService productService;

    public OrderItemService(OrderItemRepository orderItemRepository,
                          OrderService orderService,
                          ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.orderService = orderService;
        this.productService = productService;
    }

    public OrderItemDTO addItemToOrder(OrderItemDTO orderItemDTO) {
        try {
            // Validate required fields
            if (orderItemDTO.getOrderId() == null) {
                throw new GlobalException("Order ID is required");
            }
            if (orderItemDTO.getProductId() == null) {
                throw new GlobalException("Product ID is required");
            }
            if (orderItemDTO.getQuantity() == null || orderItemDTO.getQuantity() <= 0) {
                throw new GlobalException("Quantity must be greater than 0");
            }

            Order order = orderService.getOrderEntityById(orderItemDTO.getOrderId());
            Product product = productService.getProductEntityById(orderItemDTO.getProductId());

            // Check if item already exists in order
//            OrderItem existingItem = orderItemRepository.findByOrderAndProduct(order, product);
//            if (existingItem != null) {
//                // Update quantity if item already exists
//                existingItem.setQuantity(existingItem.getQuantity() + orderItemDTO.getQuantity());
//                OrderItem updatedItem = orderItemRepository.save(existingItem);
//                return convertToDTO(updatedItem);
//            }

            // Create new order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setDiscountAmount(BigDecimal.ZERO);

            OrderItem savedItem = orderItemRepository.save(orderItem);
            return convertToDTO(savedItem);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to add item to order", e);
        }
    }

    public OrderItemDTO updateOrderItemQuantity(int id, int quantity) {
        try {
            if (quantity <= 0) {
                throw new GlobalException("Quantity must be greater than 0");
            }

            OrderItem orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + id));

            orderItem.setQuantity(quantity);
            OrderItem updatedItem = orderItemRepository.save(orderItem);
            return convertToDTO(updatedItem);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update order item quantity for id: " + id, e);
        }
    }

    public void removeItemFromOrder(int id) {
        try {
            OrderItem orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + id));

            orderItemRepository.delete(orderItem);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to remove order item with id: " + id, e);
        }
    }

    public void deleteAllByOrderId(int orderId) {
        try {
            Order order = orderService.getOrderEntityById(orderId);
            orderItemRepository.deleteByOrder(order);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete order items for order id: " + orderId, e);
        }
    }

//    @Transactional(readOnly = true)
//    public List<OrderItemDTO> getItemsByOrderId(int orderId) {
//        try {
//            Order order = orderService.getOrderEntityById(orderId);
//            return orderItemRepository.findByOrder(order).stream()
//                    .map(this::convertToDTO)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            Sentry.captureException(e);
//            throw new GlobalException("Failed to retrieve items for order id: " + orderId, e);
//        }
//    }

    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setOrderId(orderItem.getOrder().getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setDiscountAmount(orderItem.getDiscountAmount());
        return dto;
    }
}