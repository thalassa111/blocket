package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IdFormatIncorrectException extends RuntimeException {

    public IdFormatIncorrectException(String message) {
        super(message);
    }
}