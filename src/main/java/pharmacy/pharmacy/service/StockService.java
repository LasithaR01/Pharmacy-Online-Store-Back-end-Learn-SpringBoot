package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.ProductRepository;
import pharmacy.pharmacy.dao.StockRepository;
import pharmacy.pharmacy.dto.StockDTO;
import pharmacy.pharmacy.dto.stock.StockCreateRequest;
import pharmacy.pharmacy.entity.*;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final BranchService branchService;
    private final UserService userService;
    private final ProductRepository productRepository;

    public StockService(StockRepository stockRepository,
                        ProductService productService,
                        SupplierService supplierService,
                        BranchService branchService,
                        ProductRepository productRepository,
                        UserService userService) {
        this.stockRepository = stockRepository;
        this.productService = productService;
        this.supplierService = supplierService;
        this.branchService = branchService;
        this.userService = userService;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<StockDTO> getAllStockEntries() {
        return stockRepository.findAll().stream()
                .map(EntityDtoMapper::convertToStockDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StockDTO getStockById(Integer id) {
        try {
            Stock stock = stockRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Stock entry not found with id: " + id));
            return EntityDtoMapper.convertToStockDTO(stock);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve stock entry with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<StockDTO> getStockByProduct(Integer productId) {
        try {
            return stockRepository.findByProductId(productId).stream()
                    .map(EntityDtoMapper::convertToStockDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve stock entries for product id: " + productId, e);
        }
    }

    @Transactional(readOnly = true)
    public List<StockDTO> getStockByBranch(Integer branchId) {
        try {
            return stockRepository.findByBranchId(branchId).stream()
                    .map(EntityDtoMapper::convertToStockDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve stock entries for branch id: " + branchId, e);
        }
    }

    @Transactional
    public StockDTO createStock(StockCreateRequest createRequest) {
        try {
            Product product = productService.getProductEntityById(createRequest.getProductId());
            Supplier supplier = supplierService.getSupplierEntityById(createRequest.getSupplierId());

            Branch branch = null;
            if (createRequest.getBranchId() != null) {
                branch = branchService.getBranchEntityById(createRequest.getBranchId());
            }

            User approvedBy = null;
            if (createRequest.getApprovedById() != null) {
                approvedBy = userService.getUserEntityById(createRequest.getApprovedById());
            }

            Stock stock = Stock.builder()
                    .product(product)
                    .supplier(supplier)
                    .quantityAdded(createRequest.getQuantityAdded())
                    .unitCost(createRequest.getUnitCost())
                    .expiryDate(createRequest.getExpiryDate())
                    .batchNumber(createRequest.getBatchNumber())
                    .branch(branch)
                    .approvedBy(approvedBy)
                    .build();

            // Increment Product stockQuantity
            product.setStockQuantity(product.getStockQuantity() + createRequest.getQuantityAdded());

            productRepository.save(product);
            stock = stockRepository.save(stock);
            return EntityDtoMapper.convertToStockDTO(stock);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create stock entry", e);
        }
    }

    @Transactional
    public StockDTO updateStock(Integer id, StockDTO stockDTO) {
        try {
            Stock existingStock = stockRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Stock entry not found with id: " + id));

            EntityDtoMapper.updateStockFromDTO(stockDTO, existingStock);

            // Update relationships if provided
            if (stockDTO.getProductId() != null) {
                Product product = productService.getProductEntityById(stockDTO.getProductId());
                existingStock.setProduct(product);
            }
            if (stockDTO.getSupplierId() != null) {
                Supplier supplier = supplierService.getSupplierEntityById(stockDTO.getSupplierId());
                existingStock.setSupplier(supplier);
            }
            if (stockDTO.getBranchId() != null) {
                Branch branch = branchService.getBranchEntityById(stockDTO.getBranchId());
                existingStock.setBranch(branch);
            }
            if (stockDTO.getApprovedById() != null) {
                User approvedBy = userService.getUserEntityById(stockDTO.getApprovedById());
                existingStock.setApprovedBy(approvedBy);
            }

            Stock updatedStock = stockRepository.save(existingStock);
            return EntityDtoMapper.convertToStockDTO(updatedStock);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update stock entry with id: " + id, e);
        }
    }

    @Transactional
    public void deleteStock(Integer id) {
        try {
            if (!stockRepository.existsById(id)) {
                throw new ResourceNotFoundException("Stock entry not found with id: " + id);
            }
            stockRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete stock entry with id: " + id, e);
        }
    }
}