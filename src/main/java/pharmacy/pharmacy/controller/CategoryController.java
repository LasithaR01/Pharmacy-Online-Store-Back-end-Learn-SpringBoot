package pharmacy.pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.service.CategoryService;


import java.util.List;

@RestController
@RequestMapping("/category")
=======
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.exception.ProductNotFoundException;
import pharmacy.pharmacy.service.CategoryService;
import pharmacy.pharmacy.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
>>>>>>> main
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
<<<<<<< HEAD
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.saveProduct(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }
}
=======
    public Category getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Category> getCategoryBySlug(@PathVariable String slug) {
        Category category = categoryService.getCategoryBySlug(slug)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with slug: " + slug));
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.saveCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategoryById(id);
    }
}
>>>>>>> main
