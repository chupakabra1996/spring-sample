package ru.kpfu.itis.exception;

/**
 * Mail api is not available to check whether email address is valid or not
 * Can't connect to api or we're blocked or any server reason
 */
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
