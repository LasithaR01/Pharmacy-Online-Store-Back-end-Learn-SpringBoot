package pharmacy.pharmacy.exception;

public class StockSaveException extends RuntimeException {
    public StockSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
