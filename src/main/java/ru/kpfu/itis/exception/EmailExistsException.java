package ru.kpfu.itis.exception;

/**
 * Exception that occurs when email is already exist in database
 */
public class EmailExistsException extends UserRegistrationException {

    public EmailExistsException() {}

    public EmailExistsException(String message) {
        super(message);
    }

    public EmailExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
