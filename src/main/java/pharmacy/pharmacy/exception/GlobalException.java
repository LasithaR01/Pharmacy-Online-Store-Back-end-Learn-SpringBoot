package pharmacy.pharmacy.exception;

import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final Object details;

    public GlobalException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public GlobalException(String message, HttpStatus httpStatus) {
        this(message, httpStatus, "GENERIC_ERROR");
    }

    public GlobalException(String message, HttpStatus httpStatus, String errorCode) {
        this(message, httpStatus, errorCode, null, null);
    }

    public GlobalException(String message, Throwable cause) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR, "GENERIC_ERROR", null, cause);
    }

    public GlobalException(String message, HttpStatus httpStatus, Throwable cause) {
        this(message, httpStatus, "GENERIC_ERROR", null, cause);
    }

    public GlobalException(String message, HttpStatus httpStatus, String errorCode, Object details) {
        this(message, httpStatus, errorCode, details, null);
    }

    public GlobalException(String message, HttpStatus httpStatus, String errorCode, Object details, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.details = details;
    }

    // Getters
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object getDetails() {
        return details;
    }
}