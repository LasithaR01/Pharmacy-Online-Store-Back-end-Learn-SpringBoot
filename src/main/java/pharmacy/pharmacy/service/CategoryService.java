package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.CategoryRepository;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.entity.Product;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Save or update a category
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    //Get Category Id
    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }
    //Get Category Slug
    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }
    // Delete Category Id
    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
    }

}
