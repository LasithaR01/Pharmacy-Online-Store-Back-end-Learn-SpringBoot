package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.sentry.Sentry;
import pharmacy.pharmacy.dao.CategoryRepository;
import pharmacy.pharmacy.dto.CategoryDTO;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.exception.CategoryNotFoundException;
import pharmacy.pharmacy.exception.DuplicateCategoryException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all product categories")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories")
    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new RuntimeException("Failed to retrieve categories", e);
        }
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the category"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public Category getCategoryById(
        @Parameter(description = "ID of the category to be retrieved", required = true)
        UUID id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        } catch (CategoryNotFoundException e) {
            Sentry.captureException(e);
            throw e;
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new RuntimeException("Failed to retrieve category with id: " + id, e);
        }
    }

    @Operation(summary = "Create or update category", description = "Creates a new category or updates an existing one")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully saved the category"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Category with this name already exists")
    })
    public Category saveCategory(
        @Parameter(description = "Category object to be created or updated", required = true)
        Category category) throws DuplicateCategoryException {
        try {
            // Check for duplicate category name
            if (category.getId() == null && categoryRepository.existsByName(category.getName())) {
                throw new DuplicateCategoryException("Category with this name already exists");
            }

            // Handle parent category if needed
            if (category.getParentCategory() != null && category.getParentCategory().getId() != null) {
                Category parent = categoryRepository.findById(category.getParentCategory().getId())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent category not found"));
                category.setParentCategory(parent);
            }

            return categoryRepository.save(category);
        } catch (DuplicateCategoryException | CategoryNotFoundException e) {
            Sentry.captureException(e);
            throw e;
        } catch (DataIntegrityViolationException e) {
            Sentry.captureException(e);
            throw new DuplicateCategoryException("Category with this name already exists", e);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new RuntimeException("Failed to save category", e);
        }
    }

    @Operation(summary = "Create or update category from DTO", description = "Creates or updates a category using DTO")
    public Category saveOrUpdateCategory(
        @Parameter(description = "Category DTO containing category data", required = true)
        CategoryDTO categoryDTO) {
        try {
            Category category;
            if (categoryDTO.getId() != null) {
                category = categoryRepository.findById(categoryDTO.getId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryDTO.getId()));
            } else {
                category = new Category();
            }

            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());

            if (categoryDTO.getParentCategoryId() != null) {
                Category parent = categoryRepository.findById(categoryDTO.getParentCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent category not found with id: " + categoryDTO.getParentCategoryId()));
                category.setParentCategory(parent);
            } else {
                category.setParentCategory(null);
            }

            return saveCategory(category);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new RuntimeException("Failed to save or update category from DTO", e);
        }
    }

    @Operation(summary = "Delete category", description = "Deletes a category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted the category"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public void deleteCategoryById(
        @Parameter(description = "ID of the category to be deleted", required = true)
        UUID id) {
        try {
            if (!categoryRepository.existsById(id)) {
                throw new CategoryNotFoundException("Category not found with id: " + id);
            }
            categoryRepository.deleteById(id);
        } catch (CategoryNotFoundException e) {
            Sentry.captureException(e);
            throw e;
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new RuntimeException("Failed to delete category with id: " + id, e);
        }
    }
}