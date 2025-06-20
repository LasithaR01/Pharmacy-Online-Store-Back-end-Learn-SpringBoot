package pharmacy.pharmacy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.dao.CategoryRepository;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public Category createCategory(Category category) {
        if (category.getParent() != null) {
            // Ensure parent exists
            getCategoryById(category.getParent().getId());
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Integer id, Category categoryDetails) {
        Category category = getCategoryById(id);

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setParent(categoryDetails.getParent());

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getChildCategories(Integer parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Transactional(readOnly = true)
    public List<Category> searchCategories(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}