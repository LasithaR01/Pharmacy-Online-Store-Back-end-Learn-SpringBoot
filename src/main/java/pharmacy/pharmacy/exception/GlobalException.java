package pharmacy.pharmacy.exception;

public class GlobalException extends RuntimeException {
    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }
}