package pharmacy.pharmacy.mapper;

import pharmacy.pharmacy.dto.*;
import pharmacy.pharmacy.dto.product.ProductDTO;
import pharmacy.pharmacy.entity.*;
import pharmacy.pharmacy.enums.OrderStatus;
import pharmacy.pharmacy.enums.PaymentMethod;
import pharmacy.pharmacy.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDtoMapper {

    // Branch Mapper
    public static BranchDTO convertToBranchDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setName(branch.getName());
        dto.setLocation(branch.getLocation());
        dto.setContactNumber(branch.getContactNumber());
        dto.setOpeningHours(branch.getOpeningHours());
        return dto;
    }

    public static Branch convertToBranch(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setName(dto.getName());
        branch.setLocation(dto.getLocation());
        branch.setContactNumber(dto.getContactNumber());
        branch.setOpeningHours(dto.getOpeningHours());
        return branch;
    }

    // Product Mapper
    public static ProductDTO convertToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setName(product.getName());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCostPrice(product.getCostPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setReorderLevel(product.getReorderLevel());
        dto.setExpiryDate(product.getExpiryDate());
        dto.setBatchNumber(product.getBatchNumber());
        dto.setBarcode(product.getBarcode());
        dto.setIsPrescriptionRequired(product.getIsPrescriptionRequired());
        return dto;
    }

    public static Product convertToProduct(ProductDTO dto, Category category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(category);
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setReorderLevel(dto.getReorderLevel());
        product.setExpiryDate(dto.getExpiryDate());
        product.setBatchNumber(dto.getBatchNumber());
        product.setBarcode(dto.getBarcode());
        product.setIsPrescriptionRequired(dto.getIsPrescriptionRequired());
        return product;
    }

    // Category Mapper
    public static CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }
        return dto;
    }

    public static Category convertToCategory(CategoryDTO dto, Category parentCategory) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParent(parentCategory);
        return category;
    }

    // Inventory Mapper
    public static InventoryDTO convertToDTO(Inventory inventory) {
        if (inventory == null) {
            return null;
        }

        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());

        // Set product reference
        if (inventory.getProduct() != null) {
            dto.setProductId(inventory.getProduct().getId());
        }

        // Set branch reference
        if (inventory.getBranch() != null) {
            dto.setBranchId(inventory.getBranch().getId());
        }

        dto.setShelfLocation(inventory.getShelfLocation());
        dto.setStockLevel(inventory.getStockLevel());
        dto.setMinimumStockLevel(inventory.getMinimumStockLevel());
        dto.setMaximumStockLevel(inventory.getMaximumStockLevel());
        dto.setLastRestocked(inventory.getLastRestocked());
        dto.setLastUpdated(inventory.getLastUpdated());
        dto.setExpiryAlert(inventory.getExpiryAlert());
        dto.setLowStockAlert(inventory.getLowStockAlert());
        dto.setCreatedAt(inventory.getCreatedAt());

        return dto;
    }

    public static Inventory convertToEntity(InventoryDTO dto, Product product, Branch branch) {
        if (dto == null) {
            return null;
        }

        Inventory inventory = new Inventory();
        inventory.setId(dto.getId());
        inventory.setProduct(product);
        inventory.setBranch(branch);
        inventory.setShelfLocation(dto.getShelfLocation());
        inventory.setStockLevel(dto.getStockLevel());
        inventory.setMinimumStockLevel(dto.getMinimumStockLevel());
        inventory.setMaximumStockLevel(dto.getMaximumStockLevel());
        inventory.setLastRestocked(dto.getLastRestocked());
        inventory.setLastUpdated(dto.getLastUpdated());
        inventory.setExpiryAlert(dto.getExpiryAlert());
        inventory.setLowStockAlert(dto.getLowStockAlert());
        inventory.setCreatedAt(dto.getCreatedAt());

        return inventory;
    }

    // For PATCH operations where only certain fields are updated
    public static void updateEntityFromDTO(InventoryDTO dto, Inventory entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getShelfLocation() != null) {
            entity.setShelfLocation(dto.getShelfLocation());
        }
        if (dto.getStockLevel() != null) {
            entity.setStockLevel(dto.getStockLevel());
        }
        if (dto.getMinimumStockLevel() != null) {
            entity.setMinimumStockLevel(dto.getMinimumStockLevel());
        }
        if (dto.getMaximumStockLevel() != null) {
            entity.setMaximumStockLevel(dto.getMaximumStockLevel());
        }
        if (dto.getExpiryAlert() != null) {
            entity.setExpiryAlert(dto.getExpiryAlert());
        }
        if (dto.getLowStockAlert() != null) {
            entity.setLowStockAlert(dto.getLowStockAlert());
        }
    }


    //     Stock Mapper
    public static StockDTO convertToStockDTO(Stock stock) {
        if (stock == null) {
            return null;
        }

        StockDTO dto = new StockDTO();
        dto.setId(stock.getId());

        if (stock.getProduct() != null) {
            dto.setProductId(stock.getProduct().getId());
        }

        if (stock.getSupplier() != null) {
            dto.setSupplierId(stock.getSupplier().getId());
        }

        dto.setQuantityAdded(stock.getQuantityAdded());
        dto.setUnitCost(stock.getUnitCost());
        dto.setDateAdded(stock.getDateAdded());
        dto.setExpiryDate(stock.getExpiryDate());
        dto.setBatchNumber(stock.getBatchNumber());

        if (stock.getBranch() != null) {
            dto.setBranchId(stock.getBranch().getId());
        }

        if (stock.getApprovedBy() != null) {
            dto.setApprovedById(stock.getApprovedBy().getId());
        }

        return dto;
    }

    public static Stock convertToStock(StockDTO dto, Product product, Supplier supplier,
                                       Branch branch, User approvedBy) {
        if (dto == null) {
            return null;
        }

        Stock stock = new Stock();
        stock.setId(dto.getId());
        stock.setProduct(product);
//        stock.setSupplier(supplier);
        stock.setQuantityAdded(dto.getQuantityAdded());
        stock.setUnitCost(dto.getUnitCost());
        stock.setExpiryDate(dto.getExpiryDate());
        stock.setBatchNumber(dto.getBatchNumber());
        stock.setBranch(branch);
        stock.setApprovedBy(approvedBy);

        return stock;
    }

    public static void updateStockFromDTO(StockDTO dto, Stock entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getQuantityAdded() != null) {
            entity.setQuantityAdded(dto.getQuantityAdded());
        }
        if (dto.getUnitCost() != null) {
            entity.setUnitCost(dto.getUnitCost());
        }
        if (dto.getExpiryDate() != null) {
            entity.setExpiryDate(dto.getExpiryDate());
        }
        if (dto.getBatchNumber() != null) {
            entity.setBatchNumber(dto.getBatchNumber());
        }
    }
    // Customer Mapper
public static CustomerDTO convertToCustomerDTO(Customer customer) {
    if (customer == null) {
        return null;
    }

    CustomerDTO dto = new CustomerDTO();
    dto.setId(customer.getId());

    if (customer.getUser() != null) {
        dto.setUserId(customer.getUser().getId());
        dto.setUserName(customer.getUser().getName());
        dto.setUserEmail(customer.getUser().getEmail());
        dto.setUserContactNumber(customer.getUser().getContactNumber());
    }

    dto.setAddress(customer.getAddress());
    dto.setDateOfBirth(customer.getDateOfBirth());
    dto.setLoyaltyPoints(customer.getLoyaltyPoints());
    dto.setCreatedAt(customer.getCreatedAt());

    return dto;
}

public static Customer convertToCustomer(CustomerDTO dto, User user) {
    if (dto == null) {
        return null;
    }

    return Customer.builder()
            .id(dto.getId())
            .user(user)
            .address(dto.getAddress())
            .dateOfBirth(dto.getDateOfBirth())
            .loyaltyPoints(dto.getLoyaltyPoints() != null ? dto.getLoyaltyPoints() : 0)
            .build();
}

public static void updateCustomerFromDTO(CustomerDTO dto, Customer entity) {
    if (dto == null || entity == null) {
        return;
    }

    if (dto.getAddress() != null) {
        entity.setAddress(dto.getAddress());
    }
    if (dto.getDateOfBirth() != null) {
        entity.setDateOfBirth(dto.getDateOfBirth());
    }
    if (dto.getLoyaltyPoints() != null) {
        entity.setLoyaltyPoints(dto.getLoyaltyPoints());
    }
}

    // Supplier Mapper
    public static SupplierDTO convertToSupplierDTO(Supplier supplier) {
        if (supplier == null) return null;

        SupplierDTO dto = new SupplierDTO();
        dto.setId(supplier.getId());

//            if (supplier.getUser() != null) {
//                dto.setUserId(supplier.getUser().getId());
//            }

        dto.setCompanyName(supplier.getCompanyName());
        dto.setAddress(supplier.getAddress());
        dto.setTaxId(supplier.getTaxId());
        dto.setCreatedAt(supplier.getCreatedAt());

        // Additional calculated fields
        if (supplier.getStockEntries() != null) {
            dto.setTotalStockItems(supplier.getStockEntries().size());
        }

        return dto;
    }

    public static Supplier convertToSupplier(SupplierDTO dto, User user) {
        if (dto == null) return null;

        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
//            supplier.setUser(user);
        supplier.setCompanyName(dto.getCompanyName());
        supplier.setAddress(dto.getAddress());
        supplier.setTaxId(dto.getTaxId());

        return supplier;
    }

    // Order Mapper
    public static OrderDTO convertToOrderDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());

        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
            dto.setUserName(order.getUser().getFullName());
        }

        if (order.getBranch() != null) {
            dto.setBranchId(order.getBranch().getId());
            dto.setBranchName(order.getBranch().getName());
        }

        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setNotes(order.getNotes());

        if (order.getProcessedBy() != null) {
            dto.setProcessedById(order.getProcessedBy().getId());
            dto.setProcessedByName(order.getProcessedBy().getFullName());
        }

        if (order.getOrderItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                    .map(EntityDtoMapper::convertToOrderItemDTO)
                    .collect(Collectors.toList());
            dto.setOrderItems(itemDTOs);
        }

        return dto;
    }

    public static Order convertToOrder(OrderDTO dto, User user, Branch branch, User processedBy) {
        if (dto == null) {
            return null;
        }

        Order order = new Order();
        order.setId(dto.getId());
        order.setUser(user);
        order.setBranch(branch);
        order.setOrderDate(dto.getOrderDate());
        order.setTotalAmount(dto.getTotalAmount());
        order.setDiscountAmount(dto.getDiscountAmount());
        order.setTaxAmount(dto.getTaxAmount());
        order.setStatus(dto.getStatus() != null ? dto.getStatus() : OrderStatus.CART);
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : PaymentStatus.PENDING);
        order.setNotes(dto.getNotes());
        order.setProcessedBy(processedBy);

        return order;
    }

    // OrderItem Mapper
    public static OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());

        if (orderItem.getOrder() != null) {
            dto.setOrderId(orderItem.getOrder().getId());
        }

        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
            dto.setProductName(orderItem.getProduct().getName());
            dto.setProductBarcode(orderItem.getProduct().getBarcode());
        }

        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setDiscountAmount(orderItem.getDiscountAmount());
        dto.setTotalPrice(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                .subtract(orderItem.getDiscountAmount() != null ?
                        orderItem.getDiscountAmount() : BigDecimal.ZERO));

        return dto;
    }

    public static OrderItem convertToOrderItem(OrderItemDTO dto, Order order, Product product) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        orderItem.setDiscountAmount(dto.getDiscountAmount());

        return orderItem;
    }

    // For PATCH operations where only certain fields are updated
    public static void updateOrderFromDTO(OrderDTO dto, Order entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getPaymentMethod() != null) {
            entity.setPaymentMethod(dto.getPaymentMethod());
        }
        if (dto.getPaymentStatus() != null) {
            entity.setPaymentStatus(dto.getPaymentStatus());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
        if (dto.getDiscountAmount() != null) {
            entity.setDiscountAmount(dto.getDiscountAmount());
        }
        if (dto.getTaxAmount() != null) {
            entity.setTaxAmount(dto.getTaxAmount());
        }
    }

    public static void updateOrderItemFromDTO(OrderItemDTO dto, OrderItem entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getDiscountAmount() != null) {
            entity.setDiscountAmount(dto.getDiscountAmount());
        }
    }

        // Prescription Mapper
    public static PrescriptionDTO convertToPrescriptionDTO(Prescription prescription) {
        if (prescription == null) {
            return null;
        }

        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());

        if (prescription.getUser() != null) {
            dto.setUserId(prescription.getUser().getId());
            dto.setUserName(prescription.getUser().getName());
        }

        dto.setDoctorName(prescription.getDoctorName());
        dto.setDoctorContact(prescription.getDoctorContact());
        dto.setPrescriptionDate(prescription.getPrescriptionDate());
        dto.setStatus(prescription.getStatus());
        dto.setNotes(prescription.getNotes());
        dto.setDocumentUrl(prescription.getDocumentUrl());

        if (prescription.getApprovedBy() != null) {
            dto.setApprovedById(prescription.getApprovedBy().getId());
            dto.setApprovedByName(prescription.getApprovedBy().getName());
        }

        dto.setCreatedAt(prescription.getCreatedAt());
        dto.setApprovedAt(prescription.getApprovedAt());

        return dto;
    }

    public static Prescription convertToPrescription(PrescriptionDTO dto, User user, User approvedBy) {
        if (dto == null) {
            return null;
        }

        Prescription prescription = new Prescription();
        prescription.setId(dto.getId());
        prescription.setUser(user);
        prescription.setDoctorName(dto.getDoctorName());
        prescription.setDoctorContact(dto.getDoctorContact());
        prescription.setPrescriptionDate(dto.getPrescriptionDate());
        prescription.setStatus(dto.getStatus() != null ? dto.getStatus() : PrescriptionStatus.PENDING);
        prescription.setNotes(dto.getNotes());
        prescription.setDocumentUrl(dto.getDocumentUrl());
        prescription.setApprovedBy(approvedBy);
        prescription.setApprovedAt(dto.getApprovedAt());

        return prescription;
    }

        // Notification Mapper
    public static NotificationDTO convertToNotificationDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());

        if (notification.getUser() != null) {
            dto.setUserId(notification.getUser().getId());
            dto.setUserName(notification.getUser().getName());
        }

        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        dto.setNotificationType(notification.getNotificationType());
        dto.setRelatedId(notification.getRelatedId());
        dto.setCreatedAt(notification.getCreatedAt());

        // Set related entity type based on notification type
        switch (notification.getNotificationType()) {
            case ORDER:
                dto.setRelatedEntityType("Order");
                break;
            case PRESCRIPTION:
                dto.setRelatedEntityType("Prescription");
                break;
            case STOCK:
                dto.setRelatedEntityType("Stock");
                break;
            case SYSTEM:
                dto.setRelatedEntityType("System");
                break;
            default:
                dto.setRelatedEntityType("General");
        }

        return dto;
    }

    public static Notification convertToNotification(NotificationDTO dto, User user) {
        if (dto == null) {
            return null;
        }

        return Notification.builder()
                .id(dto.getId())
                .user(user)
                .title(dto.getTitle())
                .message(dto.getMessage())
                .isRead(dto.isRead())
                .notificationType(dto.getNotificationType())
                .relatedId(dto.getRelatedId())
                .build();
    }

            // Alert Mapper
        public static AlertDTO convertToAlertDTO(Alert alert) {
            if (alert == null) {
                return null;
            }

            AlertDTO dto = new AlertDTO();
            dto.setId(alert.getId());

            if (alert.getProduct() != null) {
                dto.setProductId(alert.getProduct().getId());
                dto.setProductName(alert.getProduct().getName());
            }

            if (alert.getBranch() != null) {
                dto.setBranchId(alert.getBranch().getId());
                dto.setBranchName(alert.getBranch().getName());
            }

            dto.setAlertType(alert.getAlertType());
            dto.setMessage(alert.getMessage());

            if (alert.getTriggeredBy() != null) {
                dto.setTriggeredById(alert.getTriggeredBy().getId());
                dto.setTriggeredByName(alert.getTriggeredBy().getName());
            }

            dto.setResolved(alert.isResolved());

            if (alert.getResolvedBy() != null) {
                dto.setResolvedById(alert.getResolvedBy().getId());
                dto.setResolvedByName(alert.getResolvedBy().getName());
            }

            dto.setCreatedAt(alert.getCreatedAt());
            dto.setResolvedAt(alert.getResolvedAt());
            dto.setStatus(alert.getStatus());
            dto.setCritical(alert.isCritical());

            return dto;
        }

        public static Alert convertToAlert(AlertDTO dto, Product product, Branch branch,
                                         User triggeredBy, User resolvedBy) {
            if (dto == null) {
                return null;
            }

            return Alert.builder()
                    .id(dto.getId())
                    .product(product)
                    .branch(branch)
                    .alertType(dto.getAlertType())
                    .message(dto.getMessage())
                    .triggeredBy(triggeredBy)
                    .resolved(dto.isResolved())
                    .resolvedBy(resolvedBy)
                    .createdAt(dto.getCreatedAt())
                    .resolvedAt(dto.getResolvedAt())
                    .status(dto.getStatus() != null ? dto.getStatus() : AlertStatus.ACTIVE)
                    .build();
        }

            // RestockRequest Mapper
        public static RestockRequestDTO convertToRestockRequestDTO(RestockRequest request) {
            if (request == null) {
                return null;
            }

            RestockRequestDTO dto = new RestockRequestDTO();
            dto.setId(request.getId());

            if (request.getProduct() != null) {
                dto.setProductId(request.getProduct().getId());
                dto.setProductName(request.getProduct().getName());
            }

            if (request.getBranch() != null) {
                dto.setBranchId(request.getBranch().getId());
                dto.setBranchName(request.getBranch().getName());
            }

            if (request.getRequestedBy() != null) {
                dto.setRequestedById(request.getRequestedBy().getId());
                dto.setRequestedByName(request.getRequestedBy().getName());
            }

            dto.setQuantity(request.getQuantity());
            dto.setStatus(request.getStatus());

            if (request.getSupplier() != null) {
                dto.setSupplierId(request.getSupplier().getId());
                dto.setSupplierName(request.getSupplier().getCompanyName());
            }

            dto.setNotes(request.getNotes());

            if (request.getApprovedBy() != null) {
                dto.setApprovedById(request.getApprovedBy().getId());
                dto.setApprovedByName(request.getApprovedBy().getName());
            }

            dto.setCreatedAt(request.getCreatedAt());
            dto.setApprovedAt(request.getApprovedAt());

            return dto;
        }

        public static RestockRequest convertToRestockRequest(RestockRequestDTO dto,
                                                           Product product,
                                                           Branch branch,
                                                           User requestedBy,
                                                           Supplier supplier,
                                                           User approvedBy) {
            if (dto == null) {
                return null;
            }

            return RestockRequest.builder()
                    .id(dto.getId())
                    .product(product)
                    .branch(branch)
                    .requestedBy(requestedBy)
                    .quantity(dto.getQuantity())
                    .status(dto.getStatus() != null ? dto.getStatus() : RestockStatus.PENDING)
                    .supplier(supplier)
                    .notes(dto.getNotes())
                    .approvedBy(approvedBy)
                    .approvedAt(dto.getApprovedAt())
                    .build();
        }

                // ProductAlternative Mapper
        public static ProductAlternativeDTO convertToProductAlternativeDTO(ProductAlternative alternative) {
            if (alternative == null) {
                return null;
            }

            ProductAlternativeDTO dto = new ProductAlternativeDTO();
            dto.setId(alternative.getId());

            if (alternative.getProduct() != null) {
                dto.setProductId(alternative.getProduct().getId());
                dto.setProductName(alternative.getProduct().getName());
                if (alternative.getProduct().getCategory() != null) {
                    dto.setProductCategory(alternative.getProduct().getCategory().getName());
                }
            }

            if (alternative.getAlternativeProduct() != null) {
                dto.setAlternativeProductId(alternative.getAlternativeProduct().getId());
                dto.setAlternativeProductName(alternative.getAlternativeProduct().getName());
                if (alternative.getAlternativeProduct().getCategory() != null) {
                    dto.setAlternativeProductCategory(alternative.getAlternativeProduct().getCategory().getName());
                }
            }

            dto.setReason(alternative.getReason());
            dto.setCreatedAt(alternative.getCreatedAt());

            // Calculate business logic flags
            dto.setSameCategory(alternative.isSameCategory());
            dto.setCheaperAlternative(alternative.isCheaperAlternative());
            dto.setInStock(alternative.isInStock());

            return dto;
        }

        public static ProductAlternative convertToProductAlternative(ProductAlternativeDTO dto,
                                                                   Product product,
                                                                   Product alternativeProduct) {
            if (dto == null) {
                return null;
            }

            return ProductAlternative.builder()
                    .id(dto.getId())
                    .product(product)
                    .alternativeProduct(alternativeProduct)
                    .reason(dto.getReason())
                    .build();
        }

        public static void updateProductAlternativeFromDTO(ProductAlternativeDTO dto, ProductAlternative entity) {
            if (dto == null || entity == null) {
                return;
            }

            if (dto.getReason() != null) {
                entity.setReason(dto.getReason());
            }
        }

                // DrugInteraction Mapper
        public static DrugInteractionDTO convertToDrugInteractionDTO(DrugInteraction interaction) {
            if (interaction == null) {
                return null;
            }

            DrugInteractionDTO dto = new DrugInteractionDTO();
            dto.setId(interaction.getId());

            if (interaction.getProduct() != null) {
                dto.setProductId(interaction.getProduct().getId());
                dto.setProductName(interaction.getProduct().getName());
                dto.setProductCategory(interaction.getProduct().getCategory().getName());
            }

            if (interaction.getInteractsWith() != null) {
                dto.setInteractsWithId(interaction.getInteractsWith().getId());
                dto.setInteractsWithName(interaction.getInteractsWith().getName());
                dto.setInteractsWithCategory(interaction.getInteractsWith().getCategory().getName());
            }

            dto.setSeverity(interaction.getSeverity());
            dto.setDescription(interaction.getDescription());
            dto.setClinicalManagement(interaction.getClinicalManagement());
            dto.setEvidenceLevel(interaction.getEvidenceLevel());
            dto.setCreatedAt(interaction.getCreatedAt());
            dto.setSevere(interaction.isSevereInteraction());

            return dto;
        }

        public static DrugInteraction convertToDrugInteraction(DrugInteractionDTO dto,
                                                              Product product,
                                                              Product interactsWith) {
            if (dto == null) {
                return null;
            }

            return DrugInteraction.builder()
                    .id(dto.getId())
                    .product(product)
                    .interactsWith(interactsWith)
                    .severity(dto.getSeverity())
                    .description(dto.getDescription())
                    .clinicalManagement(dto.getClinicalManagement())
                    .evidenceLevel(dto.getEvidenceLevel())
                    .build();
        }

                // Employee Mapper
        public static EmployeeDTO convertToEmployeeDTO(Employee employee) {
            if (employee == null) {
                return null;
            }

            EmployeeDTO dto = new EmployeeDTO();
            dto.setId(employee.getId());

            if (employee.getUser() != null) {
                dto.setUserId(employee.getUser().getId());
                dto.setUserName(employee.getUser().getName());
                dto.setUserEmail(employee.getUser().getEmail());
            }

            if (employee.getBranch() != null) {
                dto.setBranchId(employee.getBranch().getId());
                dto.setBranchName(employee.getBranch().getName());
                dto.setBranchLocation(employee.getBranch().getLocation());
            }

            dto.setPosition(employee.getPosition());
            dto.setSalary(employee.getSalary());
            dto.setHireDate(employee.getHireDate());
            dto.setCreatedAt(employee.getCreatedAt());

            return dto;
        }

        public static Employee convertToEmployee(EmployeeDTO dto, User user, Branch branch) {
            if (dto == null) {
                return null;
            }

            return Employee.builder()
                    .id(dto.getId())
                    .user(user)
                    .branch(branch)
                    .position(dto.getPosition())
                    .salary(dto.getSalary())
                    .hireDate(dto.getHireDate())
                    .build();
        }

        public static void updateEmployeeFromDTO(EmployeeDTO dto, Employee entity) {
            if (dto == null || entity == null) {
                return;
            }

            if (dto.getPosition() != null) {
                entity.setPosition(dto.getPosition());
            }
            if (dto.getSalary() != null) {
                entity.setSalary(dto.getSalary());
            }
            if (dto.getHireDate() != null) {
                entity.setHireDate(dto.getHireDate());
            }
}
}