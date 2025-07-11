package pharmacy.pharmacy.exception;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationErrorResponse {
    private String timestamp;
    private String message;
    private String details;
    private int status;
    private Map<String, String> validationErrors;
}
