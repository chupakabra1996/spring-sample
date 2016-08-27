package ru.kpfu.itis.security.registration;

import org.springframework.context.ApplicationEvent;
import ru.kpfu.itis.model.entity.User;

/**
 * ApplicationEvent implementation for registration purposes
 */

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    //request ServletContext
    private String context;

    public OnRegistrationCompleteEvent(User user, String context) {
        super(user); //user is a source, see EventObject class
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
