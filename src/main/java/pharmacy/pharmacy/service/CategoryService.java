package pharmacy.pharmacy.service;

import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.CategoryRepository;
import pharmacy.pharmacy.entity.Category;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Constructor injection
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Fetch all products
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Fetch a product by ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Save or update a product
    public Category saveProduct(Category product) {
        return categoryRepository.save(product);
    }

    // Delete a product by ID
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
