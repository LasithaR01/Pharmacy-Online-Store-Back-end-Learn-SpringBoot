package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.CategoryRepository;
import pharmacy.pharmacy.dao.ProductRepository;
import pharmacy.pharmacy.dto.ProductRequest;
import pharmacy.pharmacy.dto.ProductSaveUpdateDTO;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.exception.ProductSaveException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Fetch all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Fetch a product by ID
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    // Save or update a product
    public Product saveProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        try {
            // Generate the slug
            String slug = productRequest.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-")
                    .replaceAll("-$", "");

            Product product = new Product();
            product.setName(productRequest.getName());
            product.setCategory(category);
            product.setSlug(slug);
            product.setDescription(productRequest.getDescription());

            return productRepository.save(product);

        } catch (DataIntegrityViolationException ex) {
            // Handle database constraint violations
            throw new ProductSaveException("Database error while saving product: " + ex.getMessage(), ex);
        } catch (NullPointerException ex) {
            // Handle specific null pointer exception
            throw new ProductSaveException("Product or its name cannot be null", ex);
        } catch (Exception ex) {
            // Catch other unexpected exceptions
            throw new ProductSaveException("An unexpected error occurred while saving the product", ex);
        }
    }

    public Product saveOrUpdateProduct(ProductSaveUpdateDTO productDTO) {

        boolean isNewProduct = (productDTO.getId() == null);
        Product product;

        if (!isNewProduct) {
            product = productRepository.findById(productDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
        } else {
            product = new Product();
        }

        product.setName(productDTO.getName());
        product.setSlug(productDTO.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-")
                .replaceAll("-$", ""));
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setExpiryDate(productDTO.getExpiryDate());

        // **Assign the category**
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        } else {
            product.setCategory(null); // Allow null if category is optional
        }

        product = productRepository.save(product);
        return product;
    }

    // Delete a product by ID
    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }
}
