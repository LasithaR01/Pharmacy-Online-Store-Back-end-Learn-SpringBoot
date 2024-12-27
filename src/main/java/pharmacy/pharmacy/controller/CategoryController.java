package pharmacy.pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.service.CategoryService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category saveCategory = categoryService.saveCategory(category);
        return new ResponseEntity<>(saveCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/{slug}")
    public Optional<Category> getCategoryBySlug(@PathVariable String slug) {
        return categoryService.getCategoryBySlug(slug);
    }
}
