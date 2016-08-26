package ru.kpfu.itis.exception;

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
