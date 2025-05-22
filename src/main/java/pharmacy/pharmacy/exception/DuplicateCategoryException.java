package pharmacy.pharmacy.exception;

public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException() {
        super();
    }

    public DuplicateCategoryException(String message) {
        super(message);
    }

    public DuplicateCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateCategoryException(Throwable cause) {
        super(cause);
    }

    protected DuplicateCategoryException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}