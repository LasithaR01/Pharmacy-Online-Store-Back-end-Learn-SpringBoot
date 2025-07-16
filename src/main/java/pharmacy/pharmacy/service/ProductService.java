package pharmacy.pharmacy.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dto.product.ProductCreateRequest;
import pharmacy.pharmacy.dto.product.ProductPageResponse;
import pharmacy.pharmacy.dto.product.ProductResponse;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.dao.ProductRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> {
                    ProductResponse dto = new ProductResponse();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
                    dto.setBarcode(product.getBarcode());
                    dto.setStockQuantity(product.getStockQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public ProductPageResponse getProductPageData(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return new ProductPageResponse(product);
    }

    @Transactional(readOnly = true)
    public Product getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with barcode: " + barcode));
    }

    @Transactional
    public Product createProduct(ProductCreateRequest request) {
        // Check for duplicate barcode
        if (productRepository.existsByBarcode(request.getBarcode())) {
            throw new GlobalException(
                    "Product with this barcode already exists.",
                    HttpStatus.CONFLICT,
                    "PRODUCT_EXISTS"
            );
        }

        Category category = null;
        if(request.getCategoryId() != null) {
            category = categoryService.getCategoryById(request.getCategoryId());
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCostPrice(request.getCostPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setReorderLevel(request.getReorderLevel());
        product.setExpiryDate(request.getExpiryDate());
        product.setBatchNumber(request.getBatchNumber());
        product.setBarcode(request.getBarcode());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Integer id, ProductCreateRequest request) {
        Product product = getProductById(id);

        Category category = null;
        if(request.getCategoryId() != null) {
            category = categoryService.getCategoryById(request.getCategoryId());
        }

        product.setName(request.getName());
        product.setCategory(category);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCostPrice(request.getCostPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setReorderLevel(request.getReorderLevel());
        product.setExpiryDate(request.getExpiryDate());
        product.setBatchNumber(request.getBatchNumber());
        product.setBarcode(request.getBarcode());
//        product.setIsPrescriptionRequired(request.getIsPrescriptionRequired());

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

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findProductsBelowReorderLevel().stream()
                .map(product -> {
                    ProductResponse dto = new ProductResponse();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
                    dto.setBarcode(product.getBarcode());
                    dto.setStockQuantity(product.getStockQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getExpiringProducts() {
        LocalDate thresholdDate = LocalDate.now().plusMonths(1);
//        return productRepository.findProductsExpiringSoon(thresholdDate);
        return productRepository.findProductsExpiringSoon(thresholdDate).stream()
                .map(product -> {
                    ProductResponse dto = new ProductResponse();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
                    dto.setBarcode(product.getBarcode());
                    dto.setStockQuantity(product.getStockQuantity());
                    dto.setExpiryDate(product.getExpiryDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProductsByBranch(Integer branchId) {
        return productRepository.findLowStockProductsByBranch(branchId, 10);
    }

    public Product getProductEntityById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }
}