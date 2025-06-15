package pharmacy.pharmacy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.dao.ProductRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Product getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with barcode: " + barcode));
    }

    @Transactional
    public Product createProduct(Product product) {
        // Ensure category exists
        categoryService.getCategoryById(product.getCategory().getId());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Integer id, Product productDetails) {
        Product product = getProductById(id);

        product.setName(productDetails.getName());
        product.setCategory(productDetails.getCategory());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCostPrice(productDetails.getCostPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setReorderLevel(productDetails.getReorderLevel());
        product.setExpiryDate(productDetails.getExpiryDate());
        product.setBatchNumber(productDetails.getBatchNumber());
        product.setBarcode(productDetails.getBarcode());
        product.setIsPrescriptionRequired(productDetails.getIsPrescriptionRequired());

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Product> getPrescriptionRequiredProducts() {
        return productRepository.findByIsPrescriptionRequired(true);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        return productRepository.findProductsBelowReorderLevel();
    }

    @Transactional(readOnly = true)
    public List<Product> getExpiringProducts() {
        LocalDate thresholdDate = LocalDate.now().plusMonths(1);
        return productRepository.findProductsExpiringSoon(thresholdDate);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProductsByBranch(Integer branchId) {
        return productRepository.findLowStockProductsByBranch(branchId, 10);
    }

    public Product getProductEntityById(Integer productId) {
        return null;
    }
}