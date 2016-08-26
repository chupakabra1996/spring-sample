package ru.kpfu.itis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Server error 500
 * Mail api is not available to check whether email address is valid or not
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class EmailVerifyingException extends RuntimeException {

    public EmailVerifyingException() {
        super();
    }

    public EmailVerifyingException(String message) {
        super(message);
    }

    public EmailVerifyingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailVerifyingException(Throwable cause) {
        super(cause);
    }
}
