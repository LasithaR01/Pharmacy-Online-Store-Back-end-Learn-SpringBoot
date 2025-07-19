package pharmacy.pharmacy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dto.category.CategoryCreateDTO;
import pharmacy.pharmacy.dto.category.CategoryResponse;
import pharmacy.pharmacy.dto.product.ProductPageResponse;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.dao.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final FileStorageService fileStorageService;

    public CategoryService(CategoryRepository categoryRepository, FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryResponse dto = new CategoryResponse();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    dto.setDescription(category.getDescription());
                    dto.setImageUrl(category.getImageUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public Category createCategory(CategoryCreateDTO createDTO) {
        // Save the file if imageFile != null
        String imageUrl = null;
        if (createDTO.getImageFile() != null && !createDTO.getImageFile().isEmpty()) {
            imageUrl = fileStorageService.storeFile(createDTO.getImageFile()); // Implement this
        }

        Category category = Category.builder()
                .name(createDTO.getName())
                .description(createDTO.getDescription())
                .imageUrl(imageUrl)
                .build();

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