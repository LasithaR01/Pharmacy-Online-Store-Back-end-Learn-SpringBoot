package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.InventoryRepository;
import pharmacy.pharmacy.dto.InventoryDTO;
import pharmacy.pharmacy.entity.Inventory;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductService productService;
    private final BranchService branchService;

    public InventoryService(InventoryRepository inventoryRepository,
                          ProductService productService,
                          BranchService branchService) {
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
        this.branchService = branchService;
    }

    public List<InventoryDTO> getAllInventory() {
        try {
            return inventoryRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all inventory records", e);
        }
    }

    public InventoryDTO getInventoryById(int id) {
        try {
            Inventory inventory = inventoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found with id: " + id));
            return convertToDTO(inventory);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve inventory record with id: " + id, e);
        }
    }

    public List<InventoryDTO> getInventoryByBranch(int branchId) {
        try {
            return inventoryRepository.findByBranchId(branchId).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve inventory for branch id: " + branchId, e);
        }
    }

    public List<InventoryDTO> getInventoryByProduct(int productId) {
        try {
            return inventoryRepository.findByProductId(productId).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve inventory for product id: " + productId, e);
        }
    }

    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        try {
            // Validate required fields
            if (inventoryDTO.getProductId() == null) {
                throw new GlobalException("Product ID is required");
            }
            if (inventoryDTO.getBranchId() == null) {
                throw new GlobalException("Branch ID is required");
            }
            if (inventoryDTO.getStockLevel() == null) {
                throw new GlobalException("Stock level is required");
            }

            Inventory inventory = convertToEntity(inventoryDTO);
            Inventory savedInventory = inventoryRepository.save(inventory);
            return convertToDTO(savedInventory);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create inventory record", e);
        }
    }

    public InventoryDTO updateInventory(int id, InventoryDTO inventoryDTO) {
        try {
            Inventory existingInventory = inventoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found with id: " + id));

            if (inventoryDTO.getShelfLocation() != null) {
                existingInventory.setShelfLocation(inventoryDTO.getShelfLocation());
            }
            if (inventoryDTO.getStockLevel() != null) {
                existingInventory.setStockLevel(inventoryDTO.getStockLevel());
            }
            if (inventoryDTO.getMinimumStockLevel() != null) {
                existingInventory.setMinimumStockLevel(inventoryDTO.getMinimumStockLevel());
            }
            if (inventoryDTO.getMaximumStockLevel() != null) {
                existingInventory.setMaximumStockLevel(inventoryDTO.getMaximumStockLevel());
            }
            if (inventoryDTO.getExpiryAlert() != null) {
                existingInventory.setExpiryAlert(inventoryDTO.getExpiryAlert());
            }
            if (inventoryDTO.getLowStockAlert() != null) {
                existingInventory.setLowStockAlert(inventoryDTO.getLowStockAlert());
            }

            Inventory updatedInventory = inventoryRepository.save(existingInventory);
            return convertToDTO(updatedInventory);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update inventory record with id: " + id, e);
        }
    }

    public void deleteInventory(int id) {
        try {
            if (!inventoryRepository.existsById(id)) {
                throw new ResourceNotFoundException("Inventory record not found with id: " + id);
            }
            inventoryRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete inventory record with id: " + id, e);
        }
    }

    public InventoryDTO updateStockLevel(int id, int quantity) {
        try {
            Inventory inventory = inventoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found with id: " + id));

            inventory.updateStockLevel(quantity);

            // Check and update alerts
            if (inventory.needsRestocking()) {
                inventory.setLowStockAlert(true);
            } else {
                inventory.setLowStockAlert(false);
            }

            Inventory updatedInventory = inventoryRepository.save(inventory);
            return convertToDTO(updatedInventory);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update stock level for inventory id: " + id, e);
        }
    }

    public List<InventoryDTO> getLowStockItems() {
        try {
            return inventoryRepository.findByLowStockAlertTrue().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve low stock items", e);
        }
    }

    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProduct().getId());
        dto.setBranchId(inventory.getBranch().getId());
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

    private Inventory convertToEntity(InventoryDTO dto) {
        Inventory inventory = new Inventory();
//        inventory.setProduct(productService.getProductEntityById(dto.getProductId()));
//        inventory.setBranch(branchService.getBranchEntityById(dto.getBranchId()));
        inventory.setShelfLocation(dto.getShelfLocation());
        inventory.setStockLevel(dto.getStockLevel());
        inventory.setMinimumStockLevel(dto.getMinimumStockLevel());
        inventory.setMaximumStockLevel(dto.getMaximumStockLevel());
        inventory.setExpiryAlert(dto.getExpiryAlert());
        inventory.setLowStockAlert(dto.getLowStockAlert());
        return inventory;
    }
}