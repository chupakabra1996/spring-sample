package ru.kpfu.itis.security.registration;

import org.springframework.context.ApplicationEvent;
import ru.kpfu.itis.model.entity.User;

import java.util.Locale;

/**
 * /TODO documentation
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String url;
    private User user;
    private Locale locale;

    public OnRegistrationCompleteEvent(String url, User user, Locale locale) {
        super(user);
        this.url = url;
        this.user = user;
        this.locale = locale;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
