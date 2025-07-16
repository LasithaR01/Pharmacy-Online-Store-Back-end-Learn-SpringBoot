package pharmacy.pharmacy.controller;

import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.product.ProductCreateRequest;
import pharmacy.pharmacy.dto.product.ProductPageResponse;
import pharmacy.pharmacy.dto.product.ProductResponse;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.service.ProductService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "Endpoints for managing pharmacy products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try {
            return ResponseEntity.ok(productService.getAllProducts());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving products", e);
        }
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductPageResponse> getProductPageData(
            @Parameter(description = "ID of the product to be retrieved") @PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductPageData(id));
    }

    @Operation(summary = "Get product by barcode", description = "Retrieve a specific product by its barcode")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getProductByBarcode(
            @Parameter(description = "Barcode of the product to be retrieved") @PathVariable String barcode) {
        try {
            return ResponseEntity.ok(productService.getProductByBarcode(barcode));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving product with barcode: " + barcode, e);
        }
    }

    @Operation(summary = "Create a new product", description = "Add a new product to the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created, no body
    }

    @Operation(summary = "Update product", description = "Update an existing product's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @Parameter(description = "ID of the product to be updated") @PathVariable Integer id,
            @Parameter(description = "Updated product object") @RequestBody ProductCreateRequest request) {
        ResponseEntity.ok(productService.updateProduct(id, request));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // no body
    }

    @Operation(summary = "Delete product", description = "Remove a product from the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to be deleted") @PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error deleting product with id: " + id, e);
        }
    }

    @Operation(summary = "Get products by category", description = "Retrieve products belonging to a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "ID of the category") @PathVariable Integer categoryId) {
        try {
            return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving products by category", e);
        }
    }

    @Operation(summary = "Search products", description = "Search products by name (case-insensitive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Search term for product name") @RequestParam String name) {
        try {
            return ResponseEntity.ok(productService.searchProducts(name));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error searching products", e);
        }
    }

    @Operation(summary = "Get low stock products", description = "Retrieve products with stock below reorder level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts() {
        try {
            return ResponseEntity.ok(productService.getLowStockProducts());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving low stock products", e);
        }
    }

    @GetMapping("/export/low-stock")
    public void exportLowStockProductsToExcel(HttpServletResponse response) throws IOException {

        List<ProductResponse> lowStockProducts = productService.getLowStockProducts();

        // Create Excel workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Invoices");

        // Header row
        HSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Product Name");
        headerRow.createCell(2).setCellValue("Category Name");
        headerRow.createCell(3).setCellValue("Barcode");
        headerRow.createCell(4).setCellValue("Stocks");
        // Add more fields based on your DTO

        // Data rows
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int rowNum = 1;
        for (ProductResponse productResponse : lowStockProducts) {
            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(productResponse.getId());
            row.createCell(1).setCellValue(productResponse.getName()); // Replace with your actual fields
            row.createCell(2).setCellValue(productResponse.getCategoryName());
            row.createCell(3).setCellValue(productResponse.getBarcode());
            row.createCell(4).setCellValue(productResponse.getStockQuantity());
        }

        // Set response headers
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=low-stock-products.xls");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @Operation(summary = "Get expiring products", description = "Retrieve products that are expiring soon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/expiring")
    public ResponseEntity<List<ProductResponse>> getExpiringProducts() {
        try {
            return ResponseEntity.ok(productService.getExpiringProducts());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Error retrieving expiring products", e);
        }
    }

    @GetMapping("/export/expiring")
    public void exportExpiringSoonProductsToExcel(HttpServletResponse response) throws IOException {

        List<ProductResponse> expiringProducts = productService.getExpiringProducts();

        // Create Excel workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Invoices");

        // Header row
        HSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Product Name");
        headerRow.createCell(2).setCellValue("Expiry Date");
        headerRow.createCell(3).setCellValue("Category Name");
        headerRow.createCell(4).setCellValue("Barcode");
        headerRow.createCell(5).setCellValue("Stocks");
        // Add more fields based on your DTO

        // Data rows
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int rowNum = 1;
        for (ProductResponse productResponse : expiringProducts) {
            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(productResponse.getId());
            row.createCell(1).setCellValue(productResponse.getName()); // Replace with your actual fields
            row.createCell(2).setCellValue(productResponse.getExpiryDate());
            row.createCell(3).setCellValue(productResponse.getCategoryName());
            row.createCell(4).setCellValue(productResponse.getBarcode());
            row.createCell(5).setCellValue(productResponse.getStockQuantity());
        }

        // Set response headers
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=expiring-soon-products.xls");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}