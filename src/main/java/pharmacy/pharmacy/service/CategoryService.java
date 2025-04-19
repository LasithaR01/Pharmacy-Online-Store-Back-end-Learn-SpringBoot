package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.CategoryRepository;
import pharmacy.pharmacy.dto.CategoryDTO;
import pharmacy.pharmacy.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Fetch all categories
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    // Fetch a category by ID
    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    // Fetch a category by slug
    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    // Save or update a category
    public Category saveCategory(Category category) {
        try {
            String slug = category.getName()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9]+", "-")
                    .replaceAll("-$", "");
            category.setSlug(slug);
            return categoryRepository.save(category);

        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Category with this slug already exists.", ex);
        } catch (NullPointerException ex) {
            throw new RuntimeException("Category name cannot be null.", ex);
        }
    }

    public Category saveOrUpdateCategory(CategoryDTO categoryDTO) {
        boolean isNewCategory = (categoryDTO.getId() == null);
        Category category;

        if (!isNewCategory) {
            category = categoryRepository.findById(categoryDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } else {
            category = new Category();
        }


        category.setName(categoryDTO.getName());
        category.setSlug(categoryDTO.getSlug());
        category.setDescription(categoryDTO.getDescription());

        return categoryRepository.save(category);
    }
    public void deleteCategoryById(UUID id) {

    }
}