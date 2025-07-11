package pharmacy.pharmacy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.sentry.Sentry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.category.CategoryResponse;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "Endpoints for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Retrieve a list of all product categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving categories", e);
        }
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @Parameter(description = "ID of the category to be retrieved") @PathVariable Integer id) {
        return ResponseEntity.ok(new CategoryResponse(categoryService.getCategoryById(id)));
    }

    @Operation(summary = "Create a new category", description = "Add a new product category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Parameter(description = "Category object to be created") @RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryService.createCategory(category));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error creating category", e);
        }
    }

    @Operation(summary = "Update category", description = "Update an existing category's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "ID of the category to be updated") @PathVariable Integer id,
            @Parameter(description = "Updated category object") @RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryService.updateCategory(id, category));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error updating category with id: " + id, e);
        }
    }

    @Operation(summary = "Delete category", description = "Remove a category from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to be deleted") @PathVariable Integer id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting category with id: " + id, e);
        }
    }

    @Operation(summary = "Get child categories", description = "Retrieve all child categories of a parent category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved child categories",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<Category>> getChildCategories(
            @Parameter(description = "ID of the parent category") @PathVariable Integer parentId) {
        try {
            return ResponseEntity.ok(categoryService.getChildCategories(parentId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving child categories", e);
        }
    }

    @Operation(summary = "Search categories", description = "Search categories by name (case-insensitive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(
            @Parameter(description = "Search term for category name") @RequestParam String name) {
        try {
            return ResponseEntity.ok(categoryService.searchCategories(name));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error searching categories", e);
        }
    }
}