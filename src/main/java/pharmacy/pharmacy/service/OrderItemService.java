package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.OrderItemRepository;
import pharmacy.pharmacy.dao.ProductRepository;
import pharmacy.pharmacy.dto.OrderItemDTO;
import pharmacy.pharmacy.entity.Order;
import pharmacy.pharmacy.entity.OrderItem;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;

import java.math.BigDecimal;

@Service
@Transactional
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            ProductService productService,
                            ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    public OrderItemDTO addItemToOrder(Order order, OrderItemDTO orderItemDTO) {
        // Validate input
        if (orderItemDTO.getProductId() == null) {
            throw new GlobalException("Product ID is required");
        }
        if (orderItemDTO.getQuantity() == null || orderItemDTO.getQuantity() <= 0) {
            throw new GlobalException("Quantity must be greater than 0");
        }

        Product product = productService.getProductById(orderItemDTO.getProductId());

        // Create new order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setPrice(product.getPrice());
        orderItem.setDiscountAmount(BigDecimal.ZERO);

        OrderItem savedItem = orderItemRepository.save(orderItem);

        // Update product stock
        product.setStockQuantity(product.getStockQuantity() - orderItemDTO.getQuantity());
        productRepository.save(product);

        return convertToDTO(savedItem);
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