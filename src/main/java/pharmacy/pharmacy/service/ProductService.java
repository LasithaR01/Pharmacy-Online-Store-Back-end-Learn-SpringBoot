package pharmacy.pharmacy.service;

import org.springframework.stereotype.Service;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Constructor injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Fetch all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Fetch a product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Save or update a product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Delete a product by ID
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
