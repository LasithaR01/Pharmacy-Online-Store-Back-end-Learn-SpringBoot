package pharmacy.pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.CategoryDTO;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> category = categoryService.getAllCategory();
        return ResponseEntity.ok(category);
    }

    //
//    // Get a category by Id
//    @GetMapping("/{id}")
//    public Optional<Category> getCategoryById(@PathVariable UUID id) {
//        return categoryService.getCategoryById(id);
//    }
//
//    // Get a category by Slug
    @GetMapping("/slug/{slug}")
    public Optional<Category> getCategoryBySlug(@PathVariable String slug) {
        return categoryService.getCategoryBySlug(slug);
    }

    // Create a new category
//    @PostMapping
//    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
//        Category saveCategory = categoryService.saveCategory(category);
//        return new ResponseEntity<>(saveCategory, HttpStatus.CREATED);
//    }

    // Update an existing category
//    @PutMapping("/{id}")
//    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody Category updatedCategory) {
//        Optional<Category> existingCategory = categoryService.getCategoryById(id);
//        if (existingCategory.isPresent()) {
//            updatedCategory.setName(updatedCategory.getName());
//            updatedCategory.setDescription(updatedCategory.getDescription());
//            Category savedCategory = categoryService.saveCategory(updatedCategory);
//            return ResponseEntity.ok(savedCategory);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @RequestMapping(headers = "Accept=application/json", method = RequestMethod.POST)
    public ResponseEntity<Category> saveOrUpdate(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.saveOrUpdateCategory(categoryDTO);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //Delete a category by Id
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategoryById(id);
    }
}
