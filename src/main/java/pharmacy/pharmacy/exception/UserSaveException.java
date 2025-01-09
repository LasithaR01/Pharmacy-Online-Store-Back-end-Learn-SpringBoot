package pharmacy.pharmacy.exception;

public class UserSaveException extends RuntimeException {

    // Constructor for exception with only a message
    public UserSaveException(String message) {
        super(message);
    }

    // Constructor for exception with a message and a cause
    public UserSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
