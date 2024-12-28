package pharmacy.pharmacy.exception;

public class ProductSaveException extends RuntimeException {
    public ProductSaveException(String message) {
        super(message);
    }

    public ProductSaveException(String message, Throwable cause) {

        super(message, cause);
    }
}
