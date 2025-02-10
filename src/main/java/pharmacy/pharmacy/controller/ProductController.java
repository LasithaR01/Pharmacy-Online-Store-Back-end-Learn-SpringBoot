package pharmacy.pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.ProductDTO;
import pharmacy.pharmacy.dto.ProductRequest;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.exception.ProductNotFoundException;
import pharmacy.pharmacy.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products.stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable UUID id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @GetMapping("/slug/{slug}")
    public Optional<Product> getProductBySlug(@PathVariable String slug) {
        return productService.getProductBySlug(slug);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
        Product createdProduct = productService.saveProduct(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id) {
        productService.deleteProductById(id);
    }
}
