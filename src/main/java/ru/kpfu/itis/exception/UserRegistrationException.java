package ru.kpfu.itis.exception;

/**
 * General exception for registration flow
 */
public class UserRegistrationException extends RuntimeException {

    public UserRegistrationException() {
        super();
    }

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

}
