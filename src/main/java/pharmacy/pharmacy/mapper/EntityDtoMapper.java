package pharmacy.pharmacy.mapper;

import pharmacy.pharmacy.dto.*;
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

        // Supplier Mapper
        public static SupplierDTO convertToSupplierDTO(Supplier supplier) {
            if (supplier == null) return null;

            SupplierDTO dto = new SupplierDTO();
            dto.setId(supplier.getId());

            if (supplier.getUser() != null) {
                dto.setUserId(supplier.getUser().getId());
            }

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
            supplier.setUser(user);
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
}