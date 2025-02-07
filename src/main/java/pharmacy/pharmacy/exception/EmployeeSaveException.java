package pharmacy.pharmacy.exception;

public class EmployeeSaveException extends RuntimeException {

    public EmployeeSaveException(String message) {
        super(message);
    }

    public EmployeeSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
