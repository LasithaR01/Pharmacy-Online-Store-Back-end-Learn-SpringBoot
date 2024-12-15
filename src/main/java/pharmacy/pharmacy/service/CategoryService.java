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

<<<<<<< Updated upstream
    // Fetch a product by ID
    public Optional<Category> getCategoryById(Long id) {
=======
    // Fetch a Category by ID
    public static Optional<Category> getCategoryById(UUID id) {
>>>>>>> Stashed changes
        return categoryRepository.findById(id);
    }

    // Save or update a product
    public Category saveProduct(Category product) {
        return categoryRepository.save(product);
    }

<<<<<<< Updated upstream
    // Delete a product by ID
    public void deleteCategoryById(Long id) {
=======
    // Save or update a Category
    public Category saveCategory(CategoryRequest category) {
        try {
            // Generate the slug
            String slug = category.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-")
                    .replaceAll("-$", "");

            // Set the slug on the product entity
            category.setSlug(slug);

            return categoryRepository.save(category);

        } catch (DataIntegrityViolationException ex) {
            // Handle database constraint violations
            throw new RuntimeException("Database error while saving product: " + ex.getMessage(), ex);
        } catch (NullPointerException ex) {
            // Handle specific null pointer exception
            throw new RuntimeException("Category or its name cannot be null", ex);
        } catch (Exception ex) {
            // Catch other unexpected exceptions
            throw new RuntimeException("An unexpected error occurred while saving the product", ex);
        }
    }


    // Delete a Category by ID
    public void deleteCategoryById(UUID id) {
>>>>>>> Stashed changes
        categoryRepository.deleteById(id);
    }

    public Category saveCategory(CategoryRequest categoryRequest) {
    }
}
