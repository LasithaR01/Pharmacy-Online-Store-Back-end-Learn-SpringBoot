package pharmacy.pharmacy.exception;

public class InvalidOrderItemException extends RuntimeException {
    public InvalidOrderItemException(String message) {
        super(message);
    }
}
