package pharmacy.pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.CategoryDTO;
import pharmacy.pharmacy.dto.CategoryRequest;
import pharmacy.pharmacy.entity.Category;
<<<<<<< Updated upstream
import pharmacy.pharmacy.service.CategoryService;


import java.util.List;
=======
import pharmacy.pharmacy.exception.CategoryNotFoundException;
import pharmacy.pharmacy.service.CategoryService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
>>>>>>> Stashed changes

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        List<Category> products = categoryService.getAllCategories();
        return products.stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
<<<<<<< Updated upstream
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.saveProduct(category);
=======
    public Category getCategoryById(@PathVariable UUID id) {
        return CategoryService.getCategoryById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Category> getCategoryBySlug(@PathVariable String slug) {
        Category category= categoryService.getCategoriesBySlug(slug)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with slug: " + slug));
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest) {
        Category createdCategory = categoryService.saveCategory(categoryRequest);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
>>>>>>> Stashed changes
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }
}