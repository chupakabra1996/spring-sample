package ru.kpfu.itis.exception;

public class EmailExistsException extends UserRegistrationException {

    public EmailExistsException() {}

    public EmailExistsException(String message) {
        super(message);
    }

    public EmailExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
