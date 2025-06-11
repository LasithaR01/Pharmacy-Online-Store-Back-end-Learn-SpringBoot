package pharmacy.pharmacy.mapper;

import pharmacy.pharmacy.dto.BranchDTO;
import pharmacy.pharmacy.dto.CategoryDTO;
import pharmacy.pharmacy.dto.InventoryDTO;
import pharmacy.pharmacy.dto.ProductDTO;
import pharmacy.pharmacy.entity.Branch;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.entity.Inventory;
import pharmacy.pharmacy.entity.Product;

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

}