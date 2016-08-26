package ru.kpfu.itis.exception;

import org.springframework.mail.MailException;

public class InternetAddressException extends MailException {

    public InternetAddressException(String msg) {
        super(msg);
    }

    public InternetAddressException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
