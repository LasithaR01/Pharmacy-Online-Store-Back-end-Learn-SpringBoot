package pharmacy.pharmacy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.CategoryDTO;
import pharmacy.pharmacy.entity.Category;
import pharmacy.pharmacy.exception.CategoryNotFoundException;
import pharmacy.pharmacy.exception.DuplicateCategoryException;
import pharmacy.pharmacy.service.CategoryService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "Endpoints for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Returns a list of all product categories")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)))
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get category by ID", description = "Returns a single category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved category",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "ID of category to be retrieved", required = true)
            @PathVariable UUID id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (CategoryNotFoundException e) {
            Sentry.captureException(e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Create new category", description = "Creates a new product category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Category with this name already exists")
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Parameter(description = "Category object to be created", required = true,
                    content = @Content(schema = @Schema(implementation = Category.class)))
            @RequestBody Category category) {
        try {
            Category createdCategory = categoryService.saveCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (DuplicateCategoryException e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update or create category", description = "Updates existing category or creates new if ID is not provided")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated/created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Category not found (when updating)"),
            @ApiResponse(responseCode = "409", description = "Category with this name already exists")
    })
    @PutMapping
    public ResponseEntity<Category> saveOrUpdateCategory(
            @Parameter(description = "Category DTO containing category data", required = true,
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
            @RequestBody CategoryDTO categoryDTO) {
        try {
            Category category = categoryService.saveOrUpdateCategory(categoryDTO);
            return ResponseEntity.ok(category);
        } catch (CategoryNotFoundException e) {
            Sentry.captureException(e);
            return ResponseEntity.notFound().build();
        } catch (DuplicateCategoryException e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete category", description = "Deletes a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of category to be deleted", required = true)
            @PathVariable UUID id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.noContent().build();
        } catch (CategoryNotFoundException e) {
            Sentry.captureException(e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.badRequest().build();
        }
    }
}