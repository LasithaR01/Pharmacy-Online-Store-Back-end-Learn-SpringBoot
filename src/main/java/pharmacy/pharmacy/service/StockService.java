package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.StockRepository;
import pharmacy.pharmacy.dao.ProductRepository;
import pharmacy.pharmacy.dao.SupplierRepository;
import pharmacy.pharmacy.dto.StockCreateDTO;
import pharmacy.pharmacy.dto.StockDTO;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.entity.Stock;
import pharmacy.pharmacy.entity.Supplier;
import pharmacy.pharmacy.exception.StockNotFoundException;
import pharmacy.pharmacy.exception.StockSaveException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

//    // Fetch all stock records
//    public List<StockDTO> getAllStock() {
//        return stockRepository.findAll().stream()
//                .map(stock -> new StockDTO(
//                        stock.getId(),
//                        stock.getProduct().getId(),
//                        stock.getSupplier().getId(),
//                        stock.getQuantityAdded(),
//                        stock.getDateAdded()))
//                .collect(Collectors.toList());
//    }
//
//    // Fetch stock by ID
//    public StockDTO getStockById(UUID id) {
//        Stock stock = stockRepository.findById(id)
//                .orElseThrow(() -> new StockNotFoundException("Stock not found with ID: " + id));
//
//        return new StockDTO(
//                stock.getId(),
//                stock.getProduct().getId(),
//                stock.getSupplier().getId(),
//                stock.getQuantityAdded(),
//                stock.getDateAdded()
//        );
//    }

    // Add new stock
    public Stock addStock(UUID productId, UUID supplierId, int quantityAdded) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        try {
            Stock stock = new Stock();
            stock.setProduct(product);
            stock.setSupplier(supplier);
            stock.setQuantityAdded(quantityAdded);
            stock.setDateAdded(new java.util.Date());

            return stockRepository.save(stock);

        } catch (DataIntegrityViolationException ex) {
            throw new StockSaveException("Database error while saving stock: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new StockSaveException("An unexpected error occurred while saving stock", ex);
        }
    }

    // Update stock
    public Stock updateStock(UUID id, StockCreateDTO stockCreateDTO) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new StockNotFoundException("Stock not found with ID: " + id));

        Product product = productRepository.findById(stockCreateDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Supplier supplier = supplierRepository.findById(stockCreateDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        stock.setProduct(product);
        stock.setSupplier(supplier);
        stock.setQuantityAdded(stockCreateDTO.getQuantityAdded());

        return stockRepository.save(stock);
    }

    // Delete stock by ID
    public void deleteStockById(UUID id) {
        if (!stockRepository.existsById(id)) {
            throw new StockNotFoundException("Stock not found with ID: " + id);
        }
        stockRepository.deleteById(id);
    }

    // Implement deleteStock method
    public void deleteStock(UUID id) {
        deleteStockById(id);
    }
}
