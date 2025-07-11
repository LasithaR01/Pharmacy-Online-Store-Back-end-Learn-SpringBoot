package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.OrderItemRepository;
import pharmacy.pharmacy.dao.OrderRepository;
import pharmacy.pharmacy.dto.OrderDTO;
import pharmacy.pharmacy.dto.OrderItemDTO;
import pharmacy.pharmacy.entity.Branch;
import pharmacy.pharmacy.entity.Order;
import pharmacy.pharmacy.entity.OrderItem;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.enums.OrderStatus;
import pharmacy.pharmacy.enums.PaymentMethod;
import pharmacy.pharmacy.enums.PaymentStatus;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BranchService branchService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderItemService orderItemService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        BranchService branchService,
                        UserService userService,
                        ProductService productService,
                        OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.branchService = branchService;
        this.userService = userService;
        this.productService = productService;
        this.orderItemService = orderItemService;
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        return convertToDTO(order, orderItems);
    }

    public Order getOrderEntityById(int id) {
        try {
            return orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve order entity with id: " + id, e);
        }
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Validate input
        if (orderDTO.getBranchId() == null) {
            throw new GlobalException("Branch ID is required");
        }

        Branch branch = branchService.getBranchEntityById(orderDTO.getBranchId());
        User user = (orderDTO.getUserId() != null)
                ? userService.getUserEntityById(orderDTO.getUserId())
                : null;

        // Create Order
        Order order = new Order();
        order.setUser(user);
        order.setBranch(branch);
        order.setOrderDate(new Date());
        order.setStatus(orderDTO.getStatus());
        order.setPaymentStatus(orderDTO.getPaymentStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setDiscountAmount(orderDTO.getDiscountAmount());
        order.setTaxAmount(orderDTO.getTaxAmount());

        Order savedOrder = orderRepository.save(order);

        // Add order items
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            orderItemService.addItemToOrder(savedOrder, itemDTO);
        }

        return convertToDTO(savedOrder);
    }

    public OrderDTO updateOrderStatus(int id, OrderStatus status) {
        try {
            Order order = getOrderEntityById(id);
            order.setStatus(status);

            // If order is completed, update payment status if not already set
            if (status == OrderStatus.COMPLETED && order.getPaymentStatus() == PaymentStatus.PENDING) {
                order.setPaymentStatus(PaymentStatus.PAID);
            }

            Order updatedOrder = orderRepository.save(order);
            return convertToDTO(updatedOrder);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update order status for id: " + id, e);
        }
    }

    public OrderDTO updatePaymentStatus(int id, PaymentStatus paymentStatus) {
        try {
            Order order = getOrderEntityById(id);
            order.setPaymentStatus(paymentStatus);
            Order updatedOrder = orderRepository.save(order);
            return convertToDTO(updatedOrder);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update payment status for order id: " + id, e);
        }
    }

    public void deleteOrder(int id) {
        try {
            Order order = getOrderEntityById(id);

            // Check if order can be deleted (only CART or PENDING orders can be deleted)
            if (order.getStatus() != OrderStatus.CART && order.getStatus() != OrderStatus.PENDING) {
                throw new GlobalException("Only CART or PENDING orders can be deleted");
            }

            // First delete all order items
            orderItemRepository.deleteByOrder(order);

            // Then delete the order
            orderRepository.delete(order);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete order with id: " + id, e);
        }
    }

    public OrderDTO checkoutOrder(int id, PaymentMethod paymentMethod) {
        try {
            Order order = getOrderEntityById(id);

            // Validate order can be checked out
            if (order.getStatus() != OrderStatus.CART) {
                throw new GlobalException("Only CART orders can be checked out");
            }
            if (orderItemRepository.countByOrder(order) == 0) {
                throw new GlobalException("Cannot checkout empty order");
            }

            // Calculate totals
            calculateOrderTotals(order);

            // Update order status
            order.setStatus(OrderStatus.PENDING);
            order.setPaymentMethod(paymentMethod);
            order.setOrderDate(new Date());

            Order updatedOrder = orderRepository.save(order);
            return convertToDTO(updatedOrder);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to checkout order with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderItemDTO> getItemsByOrderId(int orderId) {
        try {
            Order order = getOrderEntityById(orderId);
            return orderItemRepository.findByOrder(order).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve items for order id: " + orderId, e);
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

    private void calculateOrderTotals(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrder(order);
        BigDecimal subtotal = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal tax = order.getTaxAmount() != null ? order.getTaxAmount() : BigDecimal.ZERO;

        order.setTotalAmount(subtotal.subtract(discount).add(tax));
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        dto.setCustomerName(order.getUser() != null ? order.getUser().getName() : "Guest");
        dto.setBranchId(order.getBranch().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setNotes(order.getNotes());
        return dto;
    }

    private OrderDTO convertToDTO(Order order, List<OrderItem> orderItems) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        dto.setBranchId(order.getBranch().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setNotes(order.getNotes());
        dto.setCustomerName(order.getUser() != null ? order.getUser().getName() : "Guest");

        // Correctly map and collect the orderItems to a list of OrderItemDTOs
        List<OrderItemDTO> itemDTOs = orderItems.stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());

        dto.setOrderItems(itemDTOs);

        return dto;
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