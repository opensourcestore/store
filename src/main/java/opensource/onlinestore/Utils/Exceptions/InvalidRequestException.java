package opensource.onlinestore.Utils.Exceptions;

import org.springframework.validation.Errors;

/**
 * Created by orbot on 29.01.16.
 */
public class InvalidRequestException extends RuntimeException {
    private Errors errors;

    public InvalidRequestException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
