package pharmacy.pharmacy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {

    private final UUID categoryId;
    private final String categoryName;

    public CategoryNotFoundException(String message) {
        super(message);
        this.categoryId = null;
        this.categoryName = null;
    }

    public CategoryNotFoundException(UUID categoryId) {
        super(String.format("Category not found with id: %s", categoryId));
        this.categoryId = categoryId;
        this.categoryName = null;
    }

    public CategoryNotFoundException(String categoryName, boolean isNameSearch) {
        super(isNameSearch
            ? String.format("Category not found with name: %s", categoryName)
            : String.format("Category not found with reference: %s", categoryName));
        this.categoryId = null;
        this.categoryName = categoryName;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; // Optimization for performance since stack trace isn't needed
    }
}